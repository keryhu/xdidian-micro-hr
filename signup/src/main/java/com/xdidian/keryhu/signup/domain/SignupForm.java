package com.xdidian.keryhu.signup.domain;

import lombok.Data;

import java.io.Serializable;


/**
 *  用户注册 web class，此class 提交后会 转换为 signupDto
 * : 2016年6月18日 下午9:14:58
 * @author : keryHu keryhu@hotmail.com
 */
@Data
public class SignupForm implements Serializable {
  private static final long serialVersionUID = 1027409993570777508L;

  private String email;
  private String phone;
  private String name;   // 员工姓名
  private String password;
  
  public SignupForm() {

    this.email = null;
    this.phone = null;
    this.name=null;
    this.password = null;
   
  }

}
