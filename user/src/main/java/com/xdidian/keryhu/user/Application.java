package com.xdidian.keryhu.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Application extends WebMvcConfigurerAdapter {

  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }


}
