package com.xdidian.keryhu.mail.stream.signup;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


/**
 * @Description : email激活接受的消息channel
 * @date : 2016年6月18日 下午9:07:07
 * @author : keryHu keryhu@hotmail.com
 */
public interface EmailActivatedInputChannel {

  String NAME = "emailActivatedInputChannel";

  @Input(NAME)
  SubscribableChannel input();

}
