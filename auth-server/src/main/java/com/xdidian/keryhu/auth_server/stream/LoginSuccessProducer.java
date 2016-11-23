/**
 * @Title: SendSource.java
 * @Package com.xdidian.keryhu.authserver.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年5月3日 下午9:20:51
 * @version V1.0
 */
package com.xdidian.keryhu.auth_server.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;



/**
 * 
 *  当登录成功后，通过spring cloud stream 发送userId出去。) 注意这个不能通过lombok 实现 constructor
 * @date : 2016年6月18日 下午8:08:04
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@EnableBinding(LoginSuccessOutputChannel.class)
public class LoginSuccessProducer {


  @Autowired
  private LoginSuccessOutputChannel channel;

  /**
   * 当用户登录成功后，直接发送当前用户的userId，如果对方接受到是uuid，则表示登录成功了，否则表示登录失败
   */
  public void send(String userId) {
    // 登录成功后，发送成功的信号

    boolean result = channel.loginSuccessOutput().send(MessageBuilder.withPayload(userId).build());

    Assert.isTrue(result, "服务器发送登录成功的userId消息失败！");

  }
}
