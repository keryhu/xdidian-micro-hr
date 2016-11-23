package com.xdidian.keryhu.auth_server.service;


import com.xdidian.keryhu.auth_server.client.AccountActivateClient;
import com.xdidian.keryhu.auth_server.client.UserClient;
import com.xdidian.keryhu.auth_server.domain.AuthUserDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;


/**
 * 
 * @Description : Auth-server的service
 * @date : 2016年6月18日 下午8:07:19
 * @author : keryHu keryhu@hotmail.com
 */

@Service("userService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

  private final UserClient userClient;
  private final AccountActivateClient accountActivateClient;
  private final LoginAttemptUserService loginAttemptUserService;
  private final MessageSource messageSource;



  /**
   * 根据id，email，phone任何一种来查询user是否存在
   * 
   * @param identity
   * @return
   */

  @Override
  public Optional<AuthUserDto> findByIdentity(String identity) {

    return Optional.of(userClient.findByIdentity(identity));
  }



  /**
   * 当用户点击登录按钮后，如果用户名不存在，或者用户名，密码不匹配，则报告的错误提示（含有剩余的试错的次数）
   */



  @Override
  public String getLoginFailMsg(HttpServletRequest request) {
    int leftTimes = loginAttemptUserService.leftLoginTimes(request);
    Object[] args = {leftTimes};
    return messageSource.getMessage("message.login.fail", args, LocaleContextHolder.getLocale());
  }



  /**
   * 
   * 如果email未激活，再登录的时候，碰到这个情况，远程调用account－activate服务器，获取此email激活是否过期和相应
   * 的token，便于前台导航到email激活页面，活着重新注册页面。
   * 
   */
  @Override
  public String getToken(String email) {
    return accountActivateClient.doWithEmailActivate(email);
  }


}
