package com.xdidian.keryhu.account_activate.service;

import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

public interface TokenExpiredService {
  
  /**
   * 当提交的验证码，过期的时候，后台需要执行的动作。,为了应用不同的场景，需要传递场景的参数，是signup，recover，还是edit
   */
  
  void executeExpired(final String account, final ApplySituation applySituation);

}
