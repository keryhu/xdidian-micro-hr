/**
 * @Title: LoginAttemptUserServiceImpl.java
 * @Package com.xdidian.keryhu.authserver.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年4月30日 下午2:21:11
 * @version V1.0
 */
package com.xdidian.keryhu.auth_server.service;

import com.xdidian.keryhu.auth_server.domain.LoginAttemptProperties;
import com.xdidian.keryhu.auth_server.domain.LoginAttemptUser;
import com.xdidian.keryhu.auth_server.repository.LoginAttemptUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * : LoginAttemptUserService interface 具体的 实现类
 * @date : 2016年6月18日 下午8:06:44
 * @author : keryHu keryhu@hotmail.com
 */

@Component("loginAttemptUserService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@EnableConfigurationProperties(LoginAttemptProperties.class)
public class LoginAttemptUserServiceImpl implements LoginAttemptUserService {

  private final LoginAttemptProperties loginAttemptProperties;
  private final LoginAttemptUserRepository repository;
  private final MessageSource messageSource;
  private final Utils utils;


  /**
   * 
   * 用户登陆失败，促发的事件，并且传递参数用户ip，和userid，此种情况是userId不为空
   * 
   * 如果IP不存在于数据库，说明是第一次登陆，那么所有的基本信息都需要新建 如果IP存在于数据库，首先判断清零时间是否到
   * 
   */


  @Override
  public void loginFail(String ip, String loginName) {

    // 如果IP不存在于数据库

    log.info(" login fail ");
    if (!repository.findByRemoteIp(ip).isPresent()) {
      LoginAttemptUser loginAttemptUser = new LoginAttemptUser();
      Optional.of(loginAttemptUser).ifPresent(e -> {
        e.setRemoteIp(ip);
        // loginName不为空，执行存储动作
        addLoginName(loginName, e);
        e.setFirstAttemptTime(LocalDateTime.now());
        e.setAlreadyAttemptTimes(1);
        e.setAlreadyLockedTimes(0);
        e.setLocked(false);
        e.setLockedTime(null);
        repository.save(e);
      });
    }
    // 否则ip存在的情况下

    else {
      repository.findByRemoteIp(ip).ifPresent(e -> {

        // 如果FirstAttemptTime 为null的话，就设置为当前时间

        if (e.getFirstAttemptTime() == null) {
          e.setFirstAttemptTime(LocalDateTime.now());
        }

        // 指定的输错自动恢复的周期时间，是否已到(例如设定了3个小时内最多输入10次，就锁定账户，那么只要3个小时内次数没有超过10次，
        // 就可以继续登录平台，过了3个小时，输入次数恢复到0，第一次输错时间恢复为null

        boolean isPeriodExpired = LocalDateTime.now()
            .isAfter(e.getFirstAttemptTime().plusHours(loginAttemptProperties.getHoursOfPerid()));
        if (isPeriodExpired) {
          // 如果已经到了恢复时间，那么就恢复到初始值＋第一次输错的时间和次数
          e.setFirstAttemptTime(LocalDateTime.now());
          e.setAlreadyAttemptTimes(1);
        } else {

          // 如果输错次数已经等同于规定的最大次数－1

          if (e.getAlreadyAttemptTimes() >= loginAttemptProperties.getMaxAttemptTimes() - 1) {
            e.setLocked(true);
            e.setLockedTime(LocalDateTime.now());
          }
          AtomicInteger atomic = new AtomicInteger(e.getAlreadyAttemptTimes());
          // 原子性＋1
          e.setAlreadyAttemptTimes(atomic.incrementAndGet());
        }
        addLoginName(loginName, e);
        repository.save(e);

      });
    }

  }


  /**
   * 用户登陆成功后，所做的具体事情，登录成功，肯定能找的用户userId，所以就纪录数据库 如果ip不存在，则无需做任何事情， ip存在的情况下，恢复默认数据
   */
  @Override
  public void loginSuccess(String ip, String userId) {


    repository.findByRemoteIp(ip).ifPresent(e -> {
      e.setFirstAttemptTime(null);
      e.setAlreadyAttemptTimes(0);
      // 如果userId不存在于数据库的情况下，保存它到数据库
      if (!repository.findByUserId(userId).isPresent()) {
        e.setUserId(userId);
      }
      repository.save(e);

    });
  }

  /**
   * isBlocked 根据当前ip判断当前远程用户是否处于被锁定24小时状态。 首先如果IP地址不存在于数据库，那么直接返回，就是 orElse 返回的 false 如果IP存在于数据库
   * ，且账户被locked(使用的Optional 的filter实现)，那么就判断锁定的时间，到现在有没有超过最大的 指定限制时间，
   * 如果没有超过最大限定时间，则直接返回true，不需要做其他任何事情。 否则，执行后续的动作，意思是超时了－－，初始化数据库，增加被锁定的时间＋1，否则就说明账户还是处于被锁定状态
   */
  @Override
  public boolean isBlocked(String ip) {

    // 锁定的状态下，看是否超时，如果没有超时则直接返回true，如果超时了，且当前处于锁定状态,则执行恢复初始化状态，且返回false，默认返回false
    
    return repository.findByRemoteIp(ip).filter(e -> e.isLocked()).map(e -> {
     
      // 如果没有超时，且locded为true，那么直接返回true，不需要做其它任何动作
      if (!isLockedTimeOut(e)) {
        return true;
      }
      // 否则就是超时了，那么就执行恢复动作
      else {
        e.setFirstAttemptTime(null);
        e.setAlreadyAttemptTimes(0);
        e.setLocked(false);
        e.setLockedTime(null);
        AtomicInteger atomic = new AtomicInteger(e.getAlreadyLockedTimes());
        // 原子性＋1
        e.setAlreadyLockedTimes(atomic.incrementAndGet());
        repository.save(e);
        return false;
      }
    }).orElse(false);

  }


  /**
   * 将loginName增加到数据库的方法。
   */
  
  private void addLoginName(String loginName, LoginAttemptUser loginAttemptUser) {

    boolean loginNameEmpty = loginName == null || loginName.isEmpty();

    if ((!loginNameEmpty) && (!loginAttemptUser.containLoginName(loginName))) {
      loginAttemptUser.addLoginName(loginName);
    }
  }

  /**
   * 判断系统中用户,此时锁定状态有没有超时
   */
  private boolean isLockedTimeOut(LoginAttemptUser loginAttemptUser) {

    return LocalDateTime.now().isAfter(
        loginAttemptUser.getLockedTime().plusHours(loginAttemptProperties.getHoursOfLock()));

  }


  /**
   * 还剩余的登录时次
   */
  @Override
  public int leftLoginTimes(HttpServletRequest request) {

    return repository.findByRemoteIp(utils.getIp(request))
        .map(e -> loginAttemptProperties.getMaxAttemptTimes() - e.getAlreadyAttemptTimes())
        .orElse(loginAttemptProperties.getMaxAttemptTimes());
  }


  @Override
  public String getBlockMsg() {

    Object[] args = {loginAttemptProperties.getHoursOfPerid(),
            loginAttemptProperties.getMaxAttemptTimes(),
            loginAttemptProperties.getHoursOfLock() };

    return messageSource.getMessage("message.login.block",args,
            LocaleContextHolder.getLocale());
  }
}
