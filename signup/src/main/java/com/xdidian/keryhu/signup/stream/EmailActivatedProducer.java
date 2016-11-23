package com.xdidian.keryhu.signup.stream;

import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * @Description : 发出"email激活" 消息的 主方法，这个方法不能和手机激活一起，因为这个不会发到手机发送接口
 * 此为发送方，接收方，有2个，分别为 account－activated 用来存储相应的数据， 另外一个就是mail－service，用来直接发送mail
 * @date : 2016年6月18日 下午9:18:07
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@EnableBinding(EmailActivatedOutputChannel.class)
@Slf4j
public class EmailActivatedProducer {

  @Autowired
  private EmailActivatedOutputChannel channel;

  public void send(CommonTokenDto dto) {

    boolean result = channel.emailActivatedOutput().send(MessageBuilder.withPayload(dto).build());

    Assert.isTrue(result, "服务器发送email激活的message消息失败！");

    log.info("会员注册成功，现在发送email激活的message出去。");
  }


}
