package com.xdidian.keryhu.account_activate.service;

import com.xdidian.keryhu.account_activate.domain.AccountMethodDto;
import com.xdidian.keryhu.account_activate.domain.NewPasswordFormDto;


/**
 * 
* @ClassName: NewPasswordService
* @Description: 当recover的时候，所需要的service
* 1 当用户提交 通过何种方法 取回验证码的时候，验证
* 2 用户验证完 验证码后，前台提交新密码的时候，所需要的service)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月10日 下午1:40:31
 */


public interface RecoverService {
  
  public void validateNewPassword(final NewPasswordFormDto dto);
  
  public void validatAccountMethod(final AccountMethodDto dto);

}
