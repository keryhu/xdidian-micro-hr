package com.xdidian.keryhu.company.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @Description : 用户注册保存数据库加密的bean
 * @date : 2016年6月18日 下午9:22:06
 * @author : keryHu keryhu@hotmail.com
 */
@Configuration
public class PasswordBcryBean {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(11);
  }


}
