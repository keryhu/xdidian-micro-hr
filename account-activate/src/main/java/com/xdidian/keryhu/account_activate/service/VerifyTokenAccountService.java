package com.xdidian.keryhu.account_activate.service;

import com.xdidian.keryhu.account_activate.domain.TokenType;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;



/**
 * @Description : 验证token是否存在于数据库 有3中情况的token需要被验证 
 * 分别为 token，resendToken resignupToken（其中resignupToken为可选）
 * @date : 2016年6月18日 下午8:57:34
 * @author : keryHu keryhu@hotmail.com
 */
public interface VerifyTokenAccountService {

  /**
   * 查看 token，resendToken或resignupToken，是否于account 匹配
   */
  
  public boolean accountTokenMatch(final String account, final String token,
                                   final TokenType tokenType, final ApplySituation applySituation);
  
  

}
