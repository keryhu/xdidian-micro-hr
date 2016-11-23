package com.xdidian.keryhu.account_activate.service.impl;

import static com.xdidian.keryhu.util.StringValidate.isEmail;

import static com.xdidian.keryhu.util.StringValidate.isPassword;
import static com.xdidian.keryhu.util.StringValidate.isPhone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.xdidian.keryhu.account_activate.client.UserClient;
import com.xdidian.keryhu.account_activate.domain.AccountMethodDto;
import com.xdidian.keryhu.account_activate.domain.NewPasswordFormDto;
import com.xdidian.keryhu.account_activate.domain.TokenType;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.RecoverService;
import com.xdidian.keryhu.account_activate.service.VerifyTokenAccountService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;


/**
 * 
* @ClassName: NewPasswordService
* @Description: TODO(当recover的时候，所需要的service
* 1 当用户提交 通过何种方法 取回验证码的时候，验证
* 2 用户验证完 验证码后，前台提交新密码的时候，所需要的service)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月10日 下午1:40:31
 */



@Component("recoverService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class RecoverServiceImpl implements RecoverService {
  
  private final UserClient userClient;
  private final CommonActivatedTokenRepository repository;
  private final VerifyTokenAccountService verifyTokenAccountService;
  
  @Override
  public void validateNewPassword(NewPasswordFormDto dto) {

  Assert.isTrue(isEmail(dto.getAccount())||isPhone(dto.getAccount()), "必需是电子邮箱或手机号");
  Assert.isTrue(isPassword(dto.getNewPassword()), "必需6-20位，字母数字两种以上组合");
  Assert.isTrue(userClient.isLoginNameExist(dto.getAccount()), "提交的账号不存在");
  
  boolean accountExist=repository
      .findByAccountAndApplySituation(dto.getAccount(),ApplySituation.RECOVER ).isPresent();
  
  Assert.isTrue(accountExist, "提交的账号不存在");

  boolean tokenExist=verifyTokenAccountService
      .accountTokenMatch(dto.getAccount(), dto.getToken(), TokenType.CONFIRM,ApplySituation.RECOVER);
  
  Assert.isTrue(tokenExist, "验证码不正确");    
  }

  @Override
  public void validatAccountMethod(AccountMethodDto dto) {
    // TODO Auto-generated method stub
    Assert.isTrue(isEmail(dto.getAccount())||isPhone(dto.getAccount()), "提供的account必需是email或者phone");
    Assert.isTrue(userClient.isLoginNameExist(dto.getAccount()), "提供account必需存在于数据库");
  }

}
