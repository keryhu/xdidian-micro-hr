package com.xdidian.keryhu.auth_server.client;


import org.springframework.stereotype.Component;

import com.xdidian.keryhu.auth_server.domain.AuthUserDto;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @Description : 针对于user-account service remote rest 服务
 * @date : 2016年6月18日 下午7:52:40
 * @author : keryHu keryhu@hotmail.com
 */
@Component
public class UserClientFallback implements UserClient {

  
  @Override
  public AuthUserDto findByIdentity(String identity) {

    return new AuthUserDto();
  }

  @Override
  public Boolean isEmailExist(@RequestParam("email") String email) {
    return false;
  }

  @Override
  public Boolean isPhoneExist(@RequestParam("phone") String phone) {
    return false;
  }


}
