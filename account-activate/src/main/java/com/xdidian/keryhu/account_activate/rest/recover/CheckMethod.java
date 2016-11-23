package com.xdidian.keryhu.account_activate.rest.recover;


import static com.xdidian.keryhu.account_activate.domain.Constants.TOKEN_EXPIRED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xdidian.keryhu.account_activate.domain.AccountMethodDto;
import com.xdidian.keryhu.account_activate.domain.CommonActivatedLocalToken;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.CommonTokenService;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.account_activate.service.RecoverService;
import com.xdidian.keryhu.account_activate.service.ResendService;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.account_activate.stream.ResendActivateProducer;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @ClassName: RecoverAndCheckMethod
 * 当用户在，密码重置，选择何种 方法 找回密码 的页面，所遇到的rest服务
 * 
 *               1 判断account 是否为email，或 phone，是否存储在于数据库，且判断method是否一致。 2
 *               验证email，是否已经激活,不管email是否激活，如果后期密码重设成功了，则将email激活改为true 3 返回前台，method，account，
 *               resendtoken，前台收到后导航到 由前面参数组成的url页面，填写验证码) )
 * @author keryhu keryhu@hotmail.com
 * @date 2016年9月10日 下午3:24:01
 */

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class CheckMethod {

  private final RecoverService recoverService;
  private final CommonActivatedTokenRepository repository;
  private final CommonTokenService commonTokenService;
  private final TokenExpiredService tokenExpiredService;
  private final ResendService resendService;
  private final ConverterUtil converterUtil;
  private final ResendActivateProducer resendActivateProducer;

  @PostMapping("/accountActivate/recover/checkMethod")
  public ResponseEntity<?> submitMethod(@RequestBody final AccountMethodDto dto) {
    // 验证提交的信息
    Map<String, String> map = new HashMap<String, String>();

    recoverService.validatAccountMethod(dto);
    boolean accountAndApplySituationExist = repository
        .findByAccountAndApplySituation(dto.getAccount(), ApplySituation.RECOVER).isPresent();

    // 如果account存在于本地数据库，且是 recover模式。
    if (accountAndApplySituationExist) {
      // 如果token已经过期，那么执行类似 resend click的操作
      if (!commonTokenService.isCodeExired(dto.getAccount(),ApplySituation.RECOVER)) {
        resendService.clickResend(dto.getAccount(), ApplySituation.RECOVER);
        // 需要重设的url 参数，并且返回到前台，让前台去更新相关数据。
        Map<String, String> r =
            resendService.resetUrlParams(dto.getAccount(), ApplySituation.RECOVER);
        log.info("用户想要重新设置密码的时候，发现此前已经提交过了，且验证码还未过期 ");
        r.put("method", dto.getMethod());
        r.put("account", dto.getAccount());
        return ResponseEntity.ok(r);
      }
      else {
        // 如果验证码已经过期，那么首先先删除之前的验证码纪录
        tokenExpiredService.executeExpired(dto.getAccount(), ApplySituation.RECOVER);
        map.put("result", TOKEN_EXPIRED);
        return ResponseEntity.ok(map);
      }
    } 
    else {
      // 如果account 不存在数据库

      // 将前台的提交的数据，转为 数据库保存的类型，和发送消息 出去的类型
      CommonActivatedLocalToken token =
          converterUtil.accountMethodDtoToCommonActivatedLocalToken.apply(dto);

      // 再次转为 发送消息的 message 流转的message 对象
      CommonTokenDto commonTokenDto =
          converterUtil.commonActivatedLocalTokenToCommonTokenDto.apply(token);

      // 发送message 消息出去，接受者为两个，mail 服务器和 手机发送平台，他们自己根据逻辑判断是否需要处理
      // 使用resend 消息channel发送，效果一样，节省重新新建 channel的资源
      resendActivateProducer.send(commonTokenDto);

      repository.save(token);
      Map<String, String> response = new HashMap<String, String>();
      response.put("account", dto.getAccount());
      response.put("method", dto.getMethod());
      response.put("resendToken", token.getResendToken());

      // ,返回前台，method，account， resendtoken，前台收到后导航到 由前面参数组成的url页面，填写验证码

      return ResponseEntity.ok(response);
    }
  }

}
