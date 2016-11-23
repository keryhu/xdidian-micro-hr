package com.xdidian.keryhu.account_activate.service.impl;

import static com.xdidian.keryhu.util.GeneratorRandomNum.get;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.xdidian.keryhu.account_activate.domain.ActivatedProperties;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.account_activate.service.ResendService;
import com.xdidian.keryhu.account_activate.stream.ResendActivateProducer;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("resendService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@EnableConfigurationProperties(ActivatedProperties.class)
public class ResendServiceImpl implements ResendService {

  private final ActivatedProperties activatedProperties;
  private final ConverterUtil converterUtil;
  private final CommonActivatedTokenRepository repository;
  private final ResendActivateProducer resendActivateProducer;


  @Override
  public void clickResend(String account, ApplySituation applySituation) {

    repository.findByAccountAndApplySituation(account, applySituation).ifPresent(e -> {
      // 先判断冷却时间是否已到，未到则报错,之所以需要这么设置，是因为有可能是第一次存在email数据，有可能数据库还没有
      // SendExpiryDate
      boolean clickCoolingExpired =
          e.getSendExpiryDate() == null ? true : LocalDateTime.now().isAfter(e.getSendExpiryDate());
      Assert.isTrue(clickCoolingExpired, "点击'再次发送'的冷却时间未到！");
      if (e.isLocked()) {
        log.info("click is locked ! ");
        // 查看是否超时
        boolean timeout = LocalDateTime.now()
            .isAfter(e.getLockedTime().plusHours(activatedProperties.getHoursOfLocked()));

        Assert.isTrue(timeout, "您点击的太过频繁，请稍后再试！");
        // 执行默认的恢复
        e.setSentTimes(1);
        e.setFirstTime(LocalDateTime.now());
        e.setLocked(false);
        e.setLockedTime(null);
      } else {
        // 如果是第一次登录，那么设定第一次登录时间为现在
        if (e.getFirstTime() == null) {
          e.setFirstTime(LocalDateTime.now());
        }

        // 是否已到固定的发送次数清零时间
        boolean timeToEraseSendTimes = LocalDateTime.now()
            .isAfter(e.getFirstTime().plusHours(activatedProperties.getHoursOfResetSendTimes()));

        // yes
        if (timeToEraseSendTimes) {
          e.setSentTimes(1);
          e.setFirstTime(LocalDateTime.now());
        } else {
          // 如果发送次数大于等于最低最大的发送限制－1
          if (e.getSentTimes() >= activatedProperties.getMaxSendTimes() - 1) {
            e.setLocked(true);
            e.setLockedTime(LocalDateTime.now());

          }
          // 使用原子性＋1
          AtomicInteger atomic = new AtomicInteger(e.getSentTimes());
          e.setSentTimes(atomic.incrementAndGet());
        }

        // 调用的是自建的创建随机码的方法。
        e.setToken(get(6));
        e.setResendToken(UUID.randomUUID().toString());
        if (applySituation.equals(ApplySituation.SIGNUP)) {
          e.setResignupToken(UUID.randomUUID().toString());
        }

        // 设置下次 点击 “重新发送激活邮件”的冷却时间。
        e.setSendExpiryDate(
            LocalDateTime.now().plusMinutes(activatedProperties.getMinutesOfSendCycle()));

        repository.save(e);
        CommonTokenDto d = converterUtil.commonActivatedLocalTokenToCommonTokenDto.apply(e);
        // 如果是email
        resendActivateProducer.send(d);

      }
    });

  }

  /**
   * 
   * 重新设置前台token，返回给前台
   * 
   * 
   */


  @Override
  public Map<String, String> resetUrlParams(String account, ApplySituation applySituation) {
    Map<String, String> map = new HashMap<String, String>();
    
    String resend_token = repository.findByAccountAndApplySituation(account, applySituation)
        .map(e -> e.getResendToken()).orElse("");
    
    map.put("resendToken", resend_token);

    if (applySituation.equals(ApplySituation.SIGNUP)) {
      String resignup_token =
          repository.findByAccountAndApplySituation(account, applySituation)
          .map(e -> e.getResignupToken()).orElse("");
      map.put("resignupToken", resignup_token);
    }

    return map;
  }

}
