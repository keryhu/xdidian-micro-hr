package com.xdidian.keryhu.auth_server.client;


import org.springframework.stereotype.Component;

@Component
public class AccountActivateFallback implements AccountActivateClient {

  @Override
  public String doWithEmailActivate(String email) {
    return  "";
    // TODO Auto-generated method stub
    
  }

}
