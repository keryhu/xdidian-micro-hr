/**
 * @Title: LoginSuccessOutputChannel.java
 * @Package com.xdidian.keryhu.authserver.stream
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年5月4日 下午1:37:41
 * @version V1.0
 */
package com.xdidian.keryhu.auth_server.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 
 * @Description : 设置的loginSuccess 发送消息出去的专用 channel
 * @date : 2016年6月18日 下午8:07:45
 * @author : keryHu keryhu@hotmail.com
 */
public interface LoginSuccessOutputChannel {

  // 此channel的值和 application bindings下面的值一致
  String NAME = "loginSuccessOutputChannel";

  @Output(NAME)
  MessageChannel loginSuccessOutput();

}
