package com.xdidian.keryhu.auth_server.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xdidian.keryhu.auth_server.service.LoginAttemptUserService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



/**
 * 
 *  查询当前用户 IP 是否被冻结
 * 2016年6月18日 下午7:59:56
 * @author : keryHu keryhu@hotmail.com
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IpBlockRest {

  private final LoginAttemptUserService loginAttemptService;

 

  /**
   * 前台查询本地IP地址，有没有被冻结。如果冻结了，那么显示冻结的提示信息，否则显示null
   * @param request
   * @return
   */

  @GetMapping(value = "/query/blockStatus")
  public ResponseEntity<?> queryBlockStatus(HttpServletRequest request){
    int leftTimes = loginAttemptService.leftLoginTimes(request);
    
    boolean blockStatus= (leftTimes == 0);
    String msg=blockStatus?loginAttemptService.getBlockMsg():null;
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("msg", msg);
    map.put("blockStatus",blockStatus );
    return ResponseEntity.ok().body(map);
  }

}
