package com.xdidian.keryhu.account_activate.rest;

import static com.xdidian.keryhu.account_activate.domain.Constants.TOKEN_EXPIRED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.xdidian.keryhu.account_activate.client.UserClient;
import com.xdidian.keryhu.account_activate.domain.CommonConfirmTokenDto;
import com.xdidian.keryhu.account_activate.domain.TokenType;
import com.xdidian.keryhu.account_activate.service.CommonTokenService;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;


/**
 * 
* @ClassName: ResignupRest
* @Description: 这个服务，只有在用户注册完，email激活的时候，才有用，是他独有的。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月10日 下午1:19:11
 */


@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResignupRest {
  
  private final UserClient userClient;
  private final CommonTokenService commonTokenService;
  private final TokenExpiredService tokenExpiredService;
  private final ConverterUtil converterUtil;
  
  
  @PostMapping("/accountActivate/resignup")
  public ResponseEntity<?> emailResignup(@RequestBody final CommonConfirmTokenDto dto) {
    Assert.isTrue(dto.getApplySituation()!=null, "ApplySituation 不能为空");

    Map<String, Object> map = new HashMap<String, Object>();
    Assert.isTrue(dto.getApplySituation().equals(ApplySituation.SIGNUP),"非注册，不能执行");
    // 如果是注册，需要验证这个。email,必需没有激活，否则直接返回 email已经激活

    if (dto.getApplySituation().equals(ApplySituation.SIGNUP)) {
      Assert.isTrue(!userClient.emailStatus(dto.getAccount()), "email已经激活，请直接登录！");
    }
    
    
    // 验证token和account

    commonTokenService.validateCodeAndAccount(dto, TokenType.RESIGNUP);
    
    map.put("result", TOKEN_EXPIRED);
    tokenExpiredService.executeExpired(dto.getAccount(), dto.getApplySituation());
    return ResponseEntity.ok(map);
    
  }
  

}
