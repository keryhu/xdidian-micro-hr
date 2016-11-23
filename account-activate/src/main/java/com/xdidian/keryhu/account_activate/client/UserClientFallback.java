package com.xdidian.keryhu.account_activate.client;

import org.springframework.stereotype.Component;



/**
 * 
 * @Description : feign 连接失败的一个默认方法。
 * @date : 2016年6月18日 下午8:09:32
 * @author : keryHu keryhu@hotmail.com
 */
@Component
public class UserClientFallback implements UserClient {


  /**
   * 如果调用UserAccountClient 对应的spring feign 网络失败，则此方法生效
   */
  @Override
  public Boolean isEmailExist(String email) {
    return false;
  }

  /**
   * 当局登录名loginName，前台web，ajax查询当前loginName所在的数据库，email是否激活 这个是默认的失败调用
   */
  @Override
  public Boolean emailStatus(String loginName) {
    return false;
  }
  
  @Override
  public Boolean phoneStatus(String loginName) {
    return false;
  }



  @Override
  public Boolean isPhoneExist(String phone) {
    return null;
  }

  @Override
  public Boolean isLoginNameExist(String loginName) {
    return false;
  }

  @Override
  public Boolean isIdExist(String id) {
    return false;
  }



  @Override
  public String getHashPassword(String account) {
    return "";
  }

}
