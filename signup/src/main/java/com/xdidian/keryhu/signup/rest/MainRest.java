package com.xdidian.keryhu.signup.rest;


import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;
import com.xdidian.keryhu.signup.domain.SignupForm;
import com.xdidian.keryhu.signup.service.ConverterUtil;
import com.xdidian.keryhu.signup.service.UserService;
import com.xdidian.keryhu.signup.stream.EmailActivatedProducer;
import com.xdidian.keryhu.signup.stream.SignupProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MainRest {


  private final ConverterUtil converterUtil;


  private final UserService userService;

  private final SignupProducer saveproducer;

  private final EmailActivatedProducer emailActivatedProducer;

  /**
   * 验证输入信息的合法性的方法只方法逻辑层，在此，user－account里面不做判断，以为就算黑客恶意注册了，
   * 那么他也做不了什么事情，因为接下来需要邮件验证，手机验证，营业执照验证等，而且admin会定期检测账户 在注册完后，页面自动导航到 result 页面，并且附带了
   * 2个带有email信息的链接
   */
  @PostMapping("/signup")
  public ResponseEntity<?> createUser(@RequestBody final SignupForm propertyForm,
      final RedirectAttributes attr) {

    // 验证输入信息的合法性
    userService.vlidateBeforSave(propertyForm);

    CommonTokenDto dto =
        converterUtil.signupFormToCommonTokenDto.apply(propertyForm);
    // 发送email激活的message出去，包含（6位数字随机码email激活码，uuid的resend token和 resignup token，和email）。
    emailActivatedProducer.send(dto);


    // 用户注册完，发送dto具体信息的message 给 user-account,用于保存
    saveproducer.send(converterUtil.propertyFormToSignupDto.apply(propertyForm));
    log.info("注册完，发送保存信息给user－account");
   
    //因为接下来，需要前台记住，刚刚注册用户的email和发送的email token，所以注册成功后，返回这个对象
    Map<String,String> map=new HashMap<String,String>();
    map.put("email", dto.getAccount());
    map.put("resendToken", dto.getResendToken());
    map.put("resignupToken", dto.getResignupToken());
    
    return ResponseEntity.ok(map);

  }


}
