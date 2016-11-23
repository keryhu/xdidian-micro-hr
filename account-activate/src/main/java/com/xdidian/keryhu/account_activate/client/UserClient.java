package com.xdidian.keryhu.account_activate.client;

import org.springframework.cloud.netflix.feign.FeignClient;
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
   * 用于email激活时，查询email是否存在于user数据库
   */
  
  
  @RequestMapping(value = "/users/query/isEmailExist", method = RequestMethod.GET)
  public Boolean isEmailExist(@RequestParam("email") String email);
  
  /**
   * 用于phone激活时，查询phone是否存在于user数据库
   */
  
  @RequestMapping(value = "/users/query/isPhoneExist", method = RequestMethod.GET)
  public Boolean isPhoneExist(@RequestParam("phone") String phone);

  /**
   * 当局登录名loginName，前台web，ajax查询当前loginName所在的数据库，email是否激活)
   */

  
  @RequestMapping(value = "/users/query/emailStatus", method = RequestMethod.GET)

  public Boolean emailStatus(@RequestParam("loginName") String loginName);
  
  /**
   * 当局登录名loginName，前台web，ajax查询当前loginName所在的数据库，phone是否激活)
   */

  @RequestMapping(value = "/users/query/phoneStatus", method = RequestMethod.GET)
  public Boolean phoneStatus(@RequestParam("loginName") String loginName);


  /**
   * 根据account(email 或phone ，来查询
   */
  
  @RequestMapping(value = "/users/query/isLoginNameExist", method = RequestMethod.GET)
  public Boolean  isLoginNameExist(@RequestParam("loginName") String loginName);
  
  
  /**
   * 根据唯一标志，email、phone，或user中的id，3个里任何一种，来查看数据库的user
   * @param id
   * @return
   */
  
  @RequestMapping(value = "/users/query/findById", method = RequestMethod.GET)
  public Boolean isIdExist(@RequestParam("id") String id);
  
  
  /**
   * 
  * @Title: validateUsernameAndPassword
  * 返回此account 的hashpassword)
  * @param id, 不是email，也不是phone
   */
  
 
  @RequestMapping(value="/users/getHashPassword",method = RequestMethod.GET)
  public String getHashPassword(@RequestParam("id") String id);
  
 
}


