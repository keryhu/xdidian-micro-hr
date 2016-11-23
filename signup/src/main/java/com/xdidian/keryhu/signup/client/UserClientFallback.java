package com.xdidian.keryhu.signup.client;


import org.springframework.stereotype.Component;



/**
 * @Description : userAccount rest service 方法
 * @date : 2016年6月18日 下午9:14:06
 * @author : keryHu keryhu@hotmail.com
 */
@Component
public class UserClientFallback implements UserClient {


  /**
   * 查看email是否存在
   */
  @Override
  public Boolean isEmailExist(String email) {

    return false;
  }

  /**
   * 查看phone是否存在
   */
  @Override
  public Boolean isPhoneExist(String phone) {

    return false;
  }

  

}
