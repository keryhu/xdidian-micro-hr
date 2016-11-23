package com.xdidian.keryhu.account_activate.rest;


import static com.xdidian.keryhu.account_activate.domain.Constants.TOKEN_EXPIRED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.xdidian.keryhu.account_activate.service.CommonTokenService;
import com.xdidian.keryhu.account_activate.service.ResendService;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class ForLoginRest {
  
  private final CommonTokenService commonTokenService;
  private final TokenExpiredService tokenExpiredService;
  private final ResendService resendService;

  /**
   * 
   * 这个方法是login的后台，后台遇到email未激活的用户，调用此方法
   *  如果激活超时，那么就返回roles（email不存在，默认是判断为超时，内含了自动发送消息去删除此user）
   * 如果email不存在于emailActivatedToken ，采取激活超期的方法
   * 
   */
  
  
  @GetMapping(value = "/query/emailActivate")
  
  public String doWithEmailActivate(@RequestParam String email){
    //首先要确保，email在本地有数据库记录
    Map<String, Object> map = new HashMap<String, Object>();

    Gson gson = new Gson();
    
    //查看当前email激活有没有过期，取出这个判断值，默认是true，也就是假如email不存在于数据库，那么就是激活过期。
    boolean expired=commonTokenService.isCodeExired(email,ApplySituation.SIGNUP);
    Log.info("expired is : "+expired);
     // 如果过期，那么返回这个 string
    if(expired){
      map.put("result", TOKEN_EXPIRED);
      tokenExpiredService.executeExpired(email, ApplySituation.SIGNUP);
      return TOKEN_EXPIRED;
    }
    else {
      
      //如果没有过期，那么取得下面2个值，返回，激活页面会自行判断，用户能否再次点击“重新发送”按钮。
      
      Map<String,String> m=resendService.resetUrlParams(email, ApplySituation.SIGNUP);
      Log.info("m is : "+m);
      m.put("email", email);
      
      //将2个token，专为json对象 String
      return gson.toJson(m);
      
    }      
  }
}
