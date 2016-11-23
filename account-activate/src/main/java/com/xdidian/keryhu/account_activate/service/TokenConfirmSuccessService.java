package com.xdidian.keryhu.account_activate.service;

import com.xdidian.keryhu.account_activate.domain.CommonConfirmTokenDto;

/**
 * 
* @ClassName: TokenConfirmSuccessService
* @Description: 当前台输入的 token验证码激活成功的时候，需要执行的事情。
* 
* 0  共同的方法，删除本地的 此account 所在的数据库记录
* 
* 1  如果是signup 情景下 ，发送激活成功的message 给user service，让 user service 
*    更新此account 所在的数据库的 email status为true
*    
* 2  如果是recover，不处理，会有new password 的消息单独处理
* 3  如果是edit，那么发送包含 userId，和account 的消息出去。让user service 更新)
*    
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月9日 下午8:51:17
 */


public interface TokenConfirmSuccessService {
  
   void exec(CommonConfirmTokenDto dto);
}
