package com.xdidian.keryhu.account_activate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xdidian.keryhu.account_activate.domain.CommonConfirmTokenDto;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.account_activate.service.TokenConfirmSuccessService;
import com.xdidian.keryhu.account_activate.stream.ActivatedSuccessProducer;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 当前台输入的 token验证码激活成功的时候，需要执行的事情。
 * 
 *               0 共同的方法，删除本地的 此account 所在的数据库记录
 * 
 *               1 如果是signup 情景下 ，发送激活成功的message 给user service，让 user service 更新此account 所在的数据库的
 *               email status为true
 * 
 *               2 如果是recover，不处理，会有new password 的消息单独处理 3 如果是edit，那么发送包含 userId，和account
 *               的消息出去。让user service 更新)
 * 
 * @author keryhu keryhu@hotmail.com
 * @date 2016年9月9日 下午8:51:17
 */


@Component("tokenConfirmSuccessService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConfirmSuccessServiceImpl implements TokenConfirmSuccessService {

  private final CommonActivatedTokenRepository repository;
  private final ConverterUtil converterUtil;
  private final ActivatedSuccessProducer activatedSuccessProducer;

  @Override
  public void exec(CommonConfirmTokenDto dto) {
    
    CommonTokenDto commonTokenDto = new CommonTokenDto();

    // 发送激活成功的消息出去
    if (dto.getApplySituation().equals(ApplySituation.SIGNUP)) {
      commonTokenDto.setApplySituation(ApplySituation.SIGNUP);
      commonTokenDto.setAccount(dto.getAccount());
      activatedSuccessProducer.send(commonTokenDto);
      repository.findByAccountAndApplySituation(dto.getAccount(), dto.getApplySituation())
      .ifPresent(repository::delete);
    } else if (dto.getApplySituation().equals(ApplySituation.EDIT)) {
      commonTokenDto.setApplySituation(ApplySituation.EDIT);
      commonTokenDto.setAccount(dto.getAccount());
      commonTokenDto.setUserId(dto.getUserId());
      activatedSuccessProducer.send(commonTokenDto);
      repository.findByAccountAndApplySituation(dto.getAccount(), dto.getApplySituation())
      .ifPresent(repository::delete);

    }
  

   
  }

}
