package com.xdidian.keryhu.user.stream.recover;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 
* RecoverPasswordSuccessInputChannel
* 当用户更新密码成功后，useraccount接受来自 account-activated发送的消息。
* @author keryhu  keryhu@hotmail.com
* 2016年8月26日 下午5:34:17
 */



public interface RecoverPasswordSuccessInputChannel {

  String NAME = "recoverPasswordSuccessInputChannel";

  @Input(NAME)
  SubscribableChannel input();
}
