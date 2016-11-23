package com.xdidian.keryhu.pc_gateway.rest;

import com.xdidian.keryhu.pc_gateway.client.UserClient;
import com.xdidian.keryhu.util.SecurityUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 附属的rest设置
 * @date : 2016年6月18日 下午9:10:56
 * @author : keryHu keryhu@hotmail.com
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

  @Autowired
  private final UserClient userClient;

    @GetMapping("/api/hello")
  public String hello() {
    return "Hello this is pc-gateway'hello page!";
  }

    @GetMapping("/api/test")
  public String test() {
    return "Hello this is pc-gateway'test  page , it not protected !";
  }


  @GetMapping("/api/currentUser")
  public ResponseEntity user(Principal user) {

    Map<String,Object> map=new HashMap<>();
    map.put("id",user.getName());
    map.put("roles", SecurityUtils.getAuthorities());
    map.put("name",userClient.findCurrentName());
    return ResponseEntity.ok(map);
  }
  
  


}
