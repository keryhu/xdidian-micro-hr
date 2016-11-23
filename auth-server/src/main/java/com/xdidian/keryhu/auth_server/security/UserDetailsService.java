package com.xdidian.keryhu.auth_server.security;

import com.xdidian.keryhu.auth_server.domain.AuthUserDto;
import com.xdidian.keryhu.auth_server.service.LoginAttemptUserService;
import com.xdidian.keryhu.auth_server.service.UserServiceImpl;
import com.xdidian.keryhu.auth_server.service.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @Description : 关于user的操作接口
 * @date : 2016年6月18日 下午8:05:20
 * @author : keryHu keryhu@hotmail.com
 */
@Component("userDetailsService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService {

  private final UserServiceImpl userService;
  private final Utils utils;
  private final HttpServletRequest request;
  private final LoginAttemptUserService loginAttemptUserService;
  
  /**
   * 根据用户名，查询数据库系统后台(user-account),是否存在此用户,同时增加了两个拦截器 email账户是否验证激活的拦截，如果没有则报错 请注意，block拦截器必须要放在
   * 查询用户系统之前，否则其功能会被spring security 代替。 一个loginAttempt 的拦截限制，一旦用户被冻结，则锁定账户多少小时
   * 目前此项目中，增加了messageSource，获取拦截的具体提示信息， 参数｛｝的格式，参考java.text.MessageFormat Object[] args 代表
   * message中 {0} ,{1},{2} 等信息的设置的 数组。 支持 Number Date Time String getMessage,的第二个参数，放上面的
   * Object[],，第三个参数放 默认的 message，意思是如果系统找不到 前面的 Object[] args，第三个参数就是替代品 请注意，spring
   * security里面的所有错误提示，都不能使用 Assert，
   * 
   *将 loginAttempt的逻辑，放在CustomAuthenticationProvider里
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   
    
    //⚠如果username not found ，则出现的是 对应的Id==null，而不是 对象整体为null
    
    return userService.findByIdentity(username).map(e->{
      if(e.getId()==null){
        //这个监控只能写这里，不能放在 CustomAuthenticationProvider里。
        loginAttemptUserService.loginFail(utils.getIp(request), username);
        //报错的提示信息是自定义的，含有剩余的次数。
        throw new UsernameNotFoundException(userService.getLoginFailMsg(request));
      }
      return new org.springframework.security.core.userdetails.User(e.getId(), e.getPassword(),
          true, true, true, true, getAuthorities(e));
    }).get();
 
  }

  /**
   * 获取用户的权限, 增加了roles 为null的逻辑判断。
   */
  private Collection<? extends GrantedAuthority> getAuthorities(AuthUserDto user) {

    return (user.getRoles() == null) ? null
        : (user.getRoles().stream().filter(e -> e != null)
            .map(e -> new SimpleGrantedAuthority(e.name())).collect(Collectors.toList()));
  }
}
