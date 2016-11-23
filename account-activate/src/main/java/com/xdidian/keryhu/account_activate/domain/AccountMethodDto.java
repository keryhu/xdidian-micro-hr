package com.xdidian.keryhu.account_activate.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: AccountMethodDto
* @Description: 用户取回密码，第一步，首先提交，发送验证码的方式，是email，还是 phone，另外就是提供对应的account
* 这个 class 用在第二部，前台提交，使用何种方法，取回验证码。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月24日 下午4:22:15
 */

@Getter
@Setter
public class AccountMethodDto implements Serializable{

  private static final long serialVersionUID = 869366299309337696L;
  
  
  private String account;
  
  private String method;

}
