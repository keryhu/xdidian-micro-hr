package com.xdidian.keryhu.account_activate.service.impl;

import static com.xdidian.keryhu.util.StringValidate.isEmail;
import static com.xdidian.keryhu.util.StringValidate.isPhone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.xdidian.keryhu.account_activate.client.UserClient;
import com.xdidian.keryhu.account_activate.domain.FormAccountEditDto;
import com.xdidian.keryhu.account_activate.service.AccountEditService;

import lombok.RequiredArgsConstructor;

/**
 * 
* @ClassName: AccountEditService
* @Description: TODO(当前台提交email或者phone修改的时候，所需要的 验证service)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月10日 下午5:35:49
 */


@Component("accountEditService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountEditServiceImpl implements AccountEditService {
  
  private final UserClient userClient;

  @Override
  public void validateAccountEdit(FormAccountEditDto dto) {
   
    Assert.isTrue(isEmail(dto.getAccount())||isPhone(dto.getAccount()), "必需是电子邮箱或手机号");
    boolean uExist=userClient.isIdExist(dto.getUserId());
    Assert.isTrue(uExist, "ID不存在");
    Assert.isTrue(!userClient.isLoginNameExist(dto.getAccount()), "该邮箱/手机号已经注册！");
    
  }

}
