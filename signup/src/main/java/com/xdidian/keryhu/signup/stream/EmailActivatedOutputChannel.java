package com.xdidian.keryhu.signup.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Description : email 激活的 发出消息的 messageChannel
 * @date : 2016年6月18日 下午9:17:49
 * @author : keryHu keryhu@hotmail.com
 */
public interface EmailActivatedOutputChannel {

  // 此channel的值和 application bindings下面的值一致
  String NAME = "emailActivatedOutputChannel";

  @Output(NAME)
  MessageChannel emailActivatedOutput();

}
