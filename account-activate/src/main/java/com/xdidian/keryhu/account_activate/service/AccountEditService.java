package com.xdidian.keryhu.account_activate.service;

import com.xdidian.keryhu.account_activate.domain.FormAccountEditDto;

/**
 * 
* @ClassName: AccountEditService
* 当前台提交email或者phone修改的时候，所需要的 验证service)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月10日 下午5:35:49
 */


public interface AccountEditService {
  
  public void validateAccountEdit(FormAccountEditDto dto);

}
