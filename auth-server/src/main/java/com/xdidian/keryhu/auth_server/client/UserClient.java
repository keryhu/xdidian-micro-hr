package com.xdidian.keryhu.auth_server.client;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xdidian.keryhu.auth_server.domain.AuthUserDto;

/**
 * 
 *  针对于user-account service remote rest 服务
 *   用在： 用户登录的时候，验证用户名，密码的正确与否，需要feign调用user服务器。
 *  2016年6月18日 下午7:52:10
 * @author : keryHu keryhu@hotmail.com
 */

// 这里不能使用 @GetMapping ，暂时feign 不支持

@FeignClient(name = "user", fallback = UserClientFallback.class)
public interface UserClient {

   
  /**
   * 根据唯一标志，email、phone，或user中的id，3个里任何一种，来查看数据库的user
   * @param identity
   * @return
   */
  
  @RequestMapping(value = "/users/query/findByIdentity", method = RequestMethod.GET)
  public AuthUserDto findByIdentity(@RequestParam("identity") String identity);

  /**
   * 用于前台用户登录时，查询登陆的email是否已经注册过，这个是后台调用接口 注意spring feign的返回对象，要和被spring feign的的rest controller
   * 的返回结果一致
   */
  @RequestMapping(method = RequestMethod.GET, value = "/users/query/isEmailExist")
  public Boolean isEmailExist(@RequestParam("email") String email);

  /**
   * 用于前台用户登录时，查询登陆的phone是否已经注册过，这个时调用的接口
   */
  @RequestMapping(method = RequestMethod.GET, value = "/users/query/isPhoneExist")
  public Boolean isPhoneExist(@RequestParam("phone") String phone);


 
}


