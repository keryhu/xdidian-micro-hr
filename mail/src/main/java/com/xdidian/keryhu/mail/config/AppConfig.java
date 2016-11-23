package com.xdidian.keryhu.mail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;


/**
 * @Description : app配置的启动Bean
 * @date : 2016年6月18日 下午9:04:20
 * @author : keryHu keryhu@hotmail.com
 */
@Configuration
public class AppConfig {

  /**
   * 配置java 8 time thymeleaf 显示
   */
  @Bean
  public Java8TimeDialect java8TimeDialect() {
    return new Java8TimeDialect();
  }

}
