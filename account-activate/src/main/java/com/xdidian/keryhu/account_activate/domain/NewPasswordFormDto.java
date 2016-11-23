package com.xdidian.keryhu.account_activate.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
*  NewPasswordFormDto
* 当前台  提交新密码的时候，提交的resonponseBody ，这个就是主class
* @author keryhu  keryhu@hotmail.com
* 2016年8月26日 下午5:09:05
 */

@Getter
@Setter
public class NewPasswordFormDto implements Serializable{

  
  private static final long serialVersionUID = 2910391461166928714L;
  
  private String account;
  private String token;
  private String newPassword;
  
  

}
