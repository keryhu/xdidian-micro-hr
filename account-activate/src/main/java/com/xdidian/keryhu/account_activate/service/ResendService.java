package com.xdidian.keryhu.account_activate.service;

import java.util.Map;

import com.xdidian.keryhu.account_activate.domain.CommonConfirmTokenDto;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

/**
 * 
* @ClassName: ResendService
* @Description: TODO(适用于3中情景的，，resend 服务。
* 1 click 服务
* 2 reset url 参数，公用的参数是resendToken，其中signup的话，需要返回给前台resignupToken)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月10日 上午10:37:18
 */


public interface ResendService {
  
  // 3中情景，都会存在此click服务
  public void clickResend(final String account, final ApplySituation applySituation);
  
  public Map<String, String> resetUrlParams(final String account, final ApplySituation applySituation);

}
