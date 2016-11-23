package com.xdidian.keryhu.account_activate.service.impl;

import com.xdidian.keryhu.account_activate.domain.TokenType;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.VerifyTokenAccountService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Description : 验证token是否存在于数据库 有3中情况的token需要被验证 
 * 分别为 token，resendToken resignupToken（其中resignupToken为可选
 * 这是一个通用的方法，具体需要验证哪种token（confirm，resend，resignup，试前台传回的参数 决定）
 * 和具体用在哪个情景（注册，密码找回，或用户资料修改，没有任何关系
 * 所以这个是一个通用的验证 方法。
 * @date : 2016年6月18日 下午9:00:06
 * @author : keryHu keryhu@hotmail.com
 */
@Component("verifyTokenAccountService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class VerifyTokenAccountServiceImpl implements VerifyTokenAccountService {

  private final CommonActivatedTokenRepository repository;
  
  @Override
  public boolean accountTokenMatch(String account, String token, TokenType tokenType,
      ApplySituation applySituation) {
    // TODO Auto-generated method stub
    
    return repository.findByAccountAndApplySituation(account,applySituation).map(e -> {

      boolean result = false;

      switch (tokenType) {
        case CONFIRM: {
          result = e.getToken() == null || e.getToken().isEmpty() ? false
              : e.getToken().equals(token);
          break;
        }
        case RESEND: {
          result = e.getResendToken() == null || e.getResendToken().isEmpty() ? false
              : e.getResendToken().equals(token);
          break;
        }
        case RESIGNUP: {
          result = e.getResignupToken() == null || e.getResignupToken().isEmpty() ? false
              : e.getResignupToken().equals(token);
          break;
        }
        default:
          break;
      }
      log.info(tokenType+"  token is exist ? "+result);
      return result;
    }).orElse(false);
  }

}
