package com.xdidian.keryhu.pc_gateway.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 
 * @Description : 针对于user-account service remote rest 服务
 * @date : 2016年6月18日 下午8:09:14
 * @author : keryHu keryhu@hotmail.com
 */
@FeignClient(name = "user", fallback = UserClientFallback.class)
public interface UserClient {


  /**
   * 登录后，返回给前台当前用户的姓名
   */
  
  
  @RequestMapping(value = "/users/findCurrentName", method = RequestMethod.GET)
  String findCurrentName();

}


