package com.xdidian.keryhu.account_activate.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * @Description : 发送含有userId或者loginName（phone或email）的消息出去，目的为了user-account删除此user
 * @date : 2016年6月18日 下午9:02:35
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@EnableBinding(RemoveUserOutputChannel.class)
@Slf4j
public class RemoveUserProducer {

  @Autowired
  private RemoveUserOutputChannel channel;

  /**
   * 这里发出的id，有可能是uuid，也可能是email或者phone， 具体如何要靠接收方user-account来判断
   */
  public void send(String id) {
    boolean result = channel.removeUser().send(MessageBuilder.withPayload(id).build());

    Assert.isTrue(result, "发送 remove user messge 失败！！");
    log.info("成功发出删除 user Email 为 {} 的message ", id);

  }

}
