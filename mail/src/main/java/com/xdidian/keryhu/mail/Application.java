package com.xdidian.keryhu.mail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



/**
 * @Description : 邮件服务器主service
 * @date : 2016年6月18日 下午9:04:06
 * @author : keryHu keryhu@hotmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}


