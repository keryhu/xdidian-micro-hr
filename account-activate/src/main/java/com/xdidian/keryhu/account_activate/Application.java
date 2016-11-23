/**
 * @Title: Application.java
 * @Package com.xdidian.keryhu.emailActivate
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年5月11日 上午10:54:19
 * @version V1.0
 */
package com.xdidian.keryhu.account_activate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 
 * @Description : 这个service主要功能： 用户根据email或者手机收到的验证码，进行对应的
 * email－token 或 phone－token验证。其中还包含了有效期的验证。
 * @date : 2016年6月18日 下午8:08:55
 * @author : keryHu keryhu@hotmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Application {

  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }


}
