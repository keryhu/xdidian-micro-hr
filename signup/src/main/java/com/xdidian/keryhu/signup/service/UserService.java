package com.xdidian.keryhu.signup.service;

import com.xdidian.keryhu.signup.domain.SignupForm;



/**
 * @Description : 物业公司注册的一些service 接口
 * @date : 2016年6月18日 下午9:17:00
 * @author : keryHu keryhu@hotmail.com
 */
public interface UserService {

  /**
   * 物业公司注册提交的web form 数据，需要经过具体合法性验证的接口
   */
  public void vlidateBeforSave(SignupForm propertyForm);


}
