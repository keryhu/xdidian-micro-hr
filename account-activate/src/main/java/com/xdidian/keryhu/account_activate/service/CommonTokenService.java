package com.xdidian.keryhu.account_activate.service;

import com.xdidian.keryhu.account_activate.domain.CommonConfirmTokenDto;
import com.xdidian.keryhu.account_activate.domain.TokenType;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

/**
 * 
* @ClassName: TokenService
* 当用户注册/用户资料修改或者忘记密码的时候，前台输入 token验证码，来验证
* ，account（email或phone）和token，是否匹配，是否token有效时，所需要的service 验证
* 1 验证前台输入的token和account，是否符合要求)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月9日 下午3:30:34
 */


public interface CommonTokenService {
  
  public void validateCodeAndAccount(final CommonConfirmTokenDto dto, final TokenType tokenType);

  //查看account 对应下的token 有没有过期
  public boolean isCodeExired(final String account, final ApplySituation applySituation);
}
