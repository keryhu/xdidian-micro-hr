package com.xdidian.keryhu.auth_server.domain;

import com.xdidian.keryhu.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 过远程res登录的时候，需要验证的选项，除了密码，password，还需要验证
 * emailActivatedSendTimes有没有超过规定的次数，不过这个次数可以通t获取， emailActivatedStatus
 * 
 * 因为目前注册时候，没有加手机验证，所以登录的时候，现在也不加 phone 是否激活的验证
 * : 2016年6月18日 下午7:53:26
 * @author : keryHu keryhu@hotmail.com
 */
@AllArgsConstructor
@Data
public class AuthUserDto implements Serializable {

  private static final long serialVersionUID = -8674622062902968568L;
  private String id;
  private String email;    //之所以要加上email，是因为login的时候，如果emailStatus为false，需要知道具体的eamil
  private String password;
  private List<Role> roles;
  private boolean emailStatus;

  public AuthUserDto(){
    this.id=null;
    this.email=null;
    this.password=null;
    this.roles=null;
    this.emailStatus=false;
  }
 
}
