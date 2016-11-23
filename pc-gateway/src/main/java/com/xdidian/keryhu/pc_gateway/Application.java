package com.xdidian.keryhu.pc_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Description : pc gateway 主程序
 * @date : 2016年6月18日 下午9:10:10
 * @author : keryHu keryhu@hotmail.com
 */
@SpringBootApplication
@EnableZuulProxy
//@EnableFeignClients
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}


