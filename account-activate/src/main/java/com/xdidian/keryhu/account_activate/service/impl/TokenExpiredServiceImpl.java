package com.xdidian.keryhu.account_activate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.account_activate.stream.RemoveUserProducer;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;

/**
 * 
* @ClassName: TokenExpiredServiceImpl
* @Description: TODO(当token验证码过期的时候，后台需要做的动作，包含了：
*   1 删除account 本地的纪录
*   2 如果是注册的话，发送message出去，要求userService删除 之前的注册user 账号
*   4 如果是 修改个人资料，因为user service 并没有保存此更新的account，所以，只需要删除本地的  account 这条数据库记录即可
*   
* )
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月25日 下午3:20:05
 */

@Component("tokenExpiredService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenExpiredServiceImpl implements TokenExpiredService{
  
  private final CommonActivatedTokenRepository repository;
  private final RemoveUserProducer removeUserProducer;

  @Override
  public void executeExpired(String account, ApplySituation applySituation) {
    // TODO Auto-generated method stub
    repository.findByAccountAndApplySituation(account, applySituation).ifPresent(e->repository.delete(e));
    if(applySituation.equals(ApplySituation.SIGNUP)){
      //要求userService删除 之前的注册user 账号
      removeUserProducer.send(account);
    }
  }

}
