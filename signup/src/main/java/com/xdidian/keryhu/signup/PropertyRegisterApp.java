package com.xdidian.keryhu.signup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @Description : 物业公司注册主程序
 * @date : 2016年6月18日 下午9:13:31
 * @author : keryHu keryhu@hotmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PropertyRegisterApp {

  public static void main(String[] args) {


    SpringApplication.run(PropertyRegisterApp.class, args);
  }


}
