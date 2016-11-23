/**
 * @Title: LoginAttemptUserService.java
 * @Package com.xdidian.keryhu.authserver.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年4月30日 下午1:58:06
 * @version V1.0
 */
package com.xdidian.keryhu.auth_server.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @Description : 设定LoginAttemptUser service 方法。
 * @date : 2016年6月18日 下午8:06:21
 * @author : keryHu keryhu@hotmail.com
 */
public interface LoginAttemptUserService {


  /**
   * 当用户试图登陆时，登陆失败促发的事件方法
   */
  public void loginFail(String ip, String loginName);

  /**
   * 当用户试图登陆时，登陆成功，促发的事件方法。
   */
  public void loginSuccess(String ip, String userId);


  /**
   * 查看当前ip地址的远程客户，是否因为过度登录失败，而被系统锁定
   */
  public boolean isBlocked(String ip);

  /**
   * 查看目前还剩下几次登录错误的次数
   */
  public int leftLoginTimes(HttpServletRequest request);
  
  public String getBlockMsg();

}
