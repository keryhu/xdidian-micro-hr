package com.xdidian.keryhu.account_activate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.xdidian.keryhu.account_activate.client.UserClient;
import com.xdidian.keryhu.account_activate.domain.CommonActivatedLocalToken;
import com.xdidian.keryhu.account_activate.domain.CommonConfirmTokenDto;
import com.xdidian.keryhu.account_activate.domain.TokenType;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.CommonTokenService;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.account_activate.service.VerifyTokenAccountService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;

import static com.xdidian.keryhu.util.StringValidate.isEmail;
import static com.xdidian.keryhu.util.StringValidate.isPhone;

import java.time.LocalDateTime;


@Component("commonTokenService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonTokenServiceImpl implements CommonTokenService {
  
  private final UserClient userClient;
  private final CommonActivatedTokenRepository repository;
  private final VerifyTokenAccountService verifyTokenAccountService;
  
  /**
   * 
  * @ClassName: TokenService
  * 当用户注册/用户资料修改或者忘记密码的时候，前台输入 token验证码，来验证
  * ，account（email或phone）和token，是否匹配，是否token有效时，所需要的service 验证
  * 1 验证前台输入的token和account，是否符合要求)
  * @author keryhu  keryhu@hotmail.com
  * @date 2016年9月9日 下午3:30:34
   */
  
  @Override
  public void validateCodeAndAccount(CommonConfirmTokenDto dto, TokenType tokenType) {

    Assert.isTrue(isEmail(dto.getAccount())||isPhone(dto.getAccount()), "必需是电子邮箱或手机号");
    if(!dto.getApplySituation().equals(ApplySituation.EDIT)){
      Assert.isTrue(userClient.isLoginNameExist(dto.getAccount()), "提交的账号不存在");
    }
    if(dto.getApplySituation().equals(ApplySituation.EDIT)){
      Assert.isTrue(userClient.isIdExist(dto.getUserId()), "userId不存在user数据库");
      Assert.isTrue(repository.findByUserId(dto.getUserId()).isPresent(), "userId不存在account-activated数据库");
    }
     
    boolean accountExist=repository
        .findByAccountAndApplySituation(dto.getAccount(),dto.getApplySituation() ).isPresent();
    
    Assert.isTrue(accountExist, "提交的账号不存在");

   
    //account 存在的情况下，token 是否也存在
    
    boolean tokenExist=verifyTokenAccountService
        .accountTokenMatch(dto.getAccount(), dto.getToken(), tokenType,dto.getApplySituation());

    Assert.isTrue(tokenExist, "验证码不正确");
    
    if(dto.getMethod()!=null&&dto.getApplySituation().equals(ApplySituation.RECOVER)){
    
      boolean methodExist=repository.findByAccountAndApplySituation(dto.getAccount(), dto.getApplySituation() )
      .map(e->e.getRecoverMethod().equals(dto.getMethod())).get();
      
      Assert.isTrue(methodExist, "method不正确");
    }
    
  }

  /**
   * 
   * 查看account 对应下的token 有没有过期
   * 因为这个方法使用之前，已经验证过了，account是存在的。所以下面的方法不会存在 空指针
   * 
   */
  
  
  @Override
  public boolean isCodeExired(String account,ApplySituation applySituation) {

    CommonActivatedLocalToken dto=repository.findByAccountAndApplySituation(account,applySituation ).get();
    
    return LocalDateTime.now().isAfter(dto.getExpiryDate());   
  }

}
