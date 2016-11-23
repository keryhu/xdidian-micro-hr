package com.xdidian.keryhu.account_activate.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 
* @ClassName: updateSuccessOutChannel
* @Description: TODO(当更新密码成功后，发送message（包含account和新的hash密码出去
* 如果userAccount 收到后会更新密码，如果是email类型的话，如果emailStatus为false，会改为true)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月26日 下午4:43:22
* 
 */


public interface RecoverPasswordSuccessOutChannel {

  String NAME = "recoverPasswordSuccessOutChannel";

  @Output(NAME)
  MessageChannel send();
}
