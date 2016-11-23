
package com.xdidian.keryhu.auth_server.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 
 * : 专门用来记录试图访问auth－service ／login 的remote 客户， 最主要的是防范黑客暴力攻击，和暴力破解账户密码。
 * : 2016年6月18日 下午7:55:36
 * @author : keryHu keryhu@hotmail.com
 */
@Data
public class LoginAttemptUser implements Serializable {


  private static final long serialVersionUID = 1056828577558318897L;

  @Id
  private String id;

  // 远程客户试图登陆的ip地址,如果系统中不存在，则新建一个
  @Indexed(unique = true)
  private String remoteIp;

  // 如果用户有过成功的登录历史，就纪录用户userId
  private String userId;

  // 尝试记录客户登陆的的登录名,(就是用户提交给 ／login 时的 username,有可能一个ip地址，尝试了多个username，那么就做成list)
  private List<String> loginName = new ArrayList<String>();

  @DateTimeFormat(iso = ISO.DATE_TIME)
  // 当alreadyAttemptTimes 为1的时候的时间点。也就是第一次登录失败的时间点
  private LocalDateTime firstAttemptTime;

  // 已经尝试过的登陆次数
  private int alreadyAttemptTimes = 0;

  // 已经被系统锁定过的次数,默认是0（就是被系统锁定24小时的次数）
  private int alreadyLockedTimes = 0;

  // 是否被系统锁定，true表示已经被系统锁定，false 表示未被锁定,默认是false
  private boolean locked = false;

  // 被系统锁定的时间点,默认未被系统锁定，所以是null
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime lockedTime = null;

  public LoginAttemptUser() {

    this.id = UUID.randomUUID().toString();
  }

  public void addLoginName(String loginName) {
    Assert.hasLength(loginName);
    this.loginName.add(loginName);
  }

  public boolean containLoginName(String loginName) {
    return (this.loginName.contains(loginName));
  }


}
