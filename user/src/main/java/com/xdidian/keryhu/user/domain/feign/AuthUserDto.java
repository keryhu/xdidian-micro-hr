package com.xdidian.keryhu.user.domain.feign;

import com.xdidian.keryhu.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @Description : user 登录的时候，需要验证的选项，出了密码，password，还需要验证
 *              emailActivatedSendTimes有没有超过规定的次数，不过这个次数可以通过远程rest获取， emailActivatedStatus
 *              是否已经email激活验证了
 *
 *
 *              供authServer 使用。登录的时候查询。
 *
 * @date : 2016年6月18日 下午9:19:25
 * @author : keryHu keryhu@hotmail.com
 */

@AllArgsConstructor
@Data
public class AuthUserDto implements Serializable {

  private static final long serialVersionUID = -4128086432158731873L;

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