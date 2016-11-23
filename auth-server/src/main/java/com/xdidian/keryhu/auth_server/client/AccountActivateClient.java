package com.xdidian.keryhu.auth_server.client;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * auth server 远程调用 account-activate 的 spring feign 方法
 * 用在： 当用户用户登录的时候，检查用户的emailStatus如果为false，那么还需要检查该用户的
 * 激活时间有没有过期，如果过了，那么就删除原来的数据，用户需要重新注册，这个结果需要返回给前台，
 * 如果没有过期，那么就导航到email激活，要求输入验证码的页面。
 * @date : 2016年7月23日 下午1:18:47
 * @author : keryHu keryhu@hotmail.com
 */
@FeignClient(name = "account-activate", fallback = AccountActivateFallback.class)
public interface AccountActivateClient {
  
  @RequestMapping(value = "/query/emailActivate", method = RequestMethod.GET)
  public String doWithEmailActivate(@RequestParam("email") String email);

}
