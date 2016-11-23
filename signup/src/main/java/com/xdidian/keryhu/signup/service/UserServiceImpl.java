package com.xdidian.keryhu.signup.service;

import com.xdidian.keryhu.signup.client.UserClient;
import com.xdidian.keryhu.signup.domain.SignupForm;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.xdidian.keryhu.util.StringValidate.*;


/**
 * @Description : 实现了UserService 的接口
 * @date : 2016年6月18日 下午9:17:17
 * @author : keryHu keryhu@hotmail.com
 */
@Component("userService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

  private final UserClient userClient;

  /**
   * 验证前台输入信息的合法性
   */
  @Override
  public void vlidateBeforSave(SignupForm propertyForm) {

    Assert.isTrue(isEmail(propertyForm.getEmail()), "email格式不正确！");
    Assert.isTrue(isPhone(propertyForm.getPhone()), "phone格式不正确！");
    Assert.isTrue(isPassword(propertyForm.getPassword()), "密码格式不正确！");
    Assert.isTrue(isPeopleName(propertyForm.getName()), "姓名格式不正确！");
    
    Assert.isTrue(!userClient.isEmailExist(propertyForm.getEmail()), "email已经注册过，请直接登录！");
    Assert.isTrue(!userClient.isPhoneExist(propertyForm.getPhone()), "phone已经注册，请直接登录！");
    

  }

}
