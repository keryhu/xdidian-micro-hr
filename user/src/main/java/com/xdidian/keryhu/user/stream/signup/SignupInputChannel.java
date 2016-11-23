package com.xdidian.keryhu.user.stream.signup;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Description : 用户注册，接受消息的channel
 * @date : 2016年6月18日 下午9:25:58
 * @author : keryHu keryhu@hotmail.com
 */
public interface SignupInputChannel {

  String NAME = "signupInputChannel";

  @Input(NAME)
  SubscribableChannel input();

}
