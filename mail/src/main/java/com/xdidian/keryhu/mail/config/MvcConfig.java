package com.xdidian.keryhu.mail.config;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xdidian.keryhu.domain.AccountActivatedDto;

/**
 * @Description : TODO(用于测试 mail thymeleaf 页面的布局。)
 * @date : 2016年7月20日 上午10:37:00
 * @author : keryHu keryhu@hotmail.com
 */
@Controller
public class MvcConfig {
  
  
  @RequestMapping("/test")
  public String get(Model model){
    AccountActivatedDto dto=new AccountActivatedDto();
    dto.setId("keryhu@hotmail.com");
    dto.setToken("12");
    dto.setResendToken("22");
    dto.setResignupToken("32");
    dto.setExpireDate(LocalDateTime.now());
    
    
    model.addAttribute("dto", dto);
    model.addAttribute("url", "222222");
    model.addAttribute("token", dto.getToken());
    
    return "emailActivated";
  }

}
