package com.xdidian.keryhu.auth_server.service;


import javax.servlet.http.HttpServletRequest;

import com.xdidian.keryhu.auth_server.domain.AuthUserDto;

import java.util.Optional;

/**
 * 
 * @Description : Auth-service的一些方法接口
 * @date : 2016年6月18日 下午8:07:00
 * @author : keryHu keryhu@hotmail.com
 */

public interface UserService {

  
  /**
   * 根据id，email，phone任何一种来查询user是否存在
   * 
   * @param identity
   * @return
   */
  
  public Optional<AuthUserDto> findByIdentity(String identity);

  
  /**
   * 当用户点击登录按钮后，如果用户名不存在，或者用户名，密码不匹配，则报告的错误提示（含有剩余的试错的次数）
   */
  
  public String getLoginFailMsg(HttpServletRequest request);
  
  
 /**
  * 
  * 如果email未激活，再登录的时候，碰到这个情况，远程调用account－activate服务器，获取此email激活是否过期和相应
  * 的token，便于前台导航到email激活页面，活着重新注册页面。
  * 
  */
  
  public String getToken(String email);

}
