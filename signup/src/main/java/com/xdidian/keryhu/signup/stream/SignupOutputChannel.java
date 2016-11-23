package com.xdidian.keryhu.signup.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;



/**
 * @Description : 物业公司注册后，保存注册信息的发送message 的channel
 * @date : 2016年6月18日 下午9:18:23
 * @author : keryHu keryhu@hotmail.com
 */
public interface SignupOutputChannel {

  String NAME = "signupOutputChannel";

  @Output(NAME)
  MessageChannel saveOutput();

}
