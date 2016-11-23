package com.xdidian.keryhu.signup.stream;

import com.xdidian.keryhu.domain.SignupDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @Description : 用户注册完后，发送此消息出去
 * @date : 2016年6月18日 下午9:18:52
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@EnableBinding(SignupOutputChannel.class)
@Slf4j
public class SignupProducer {

  @Autowired
  private SignupOutputChannel channel;

  /**
   * 当物业公司注册完，直接发送物业公司的message 出去。
   */
  public void send(SignupDto dto) {

    boolean result = channel.saveOutput().send(MessageBuilder.withPayload(dto).build());

    Assert.isTrue(result, "用户注册完，发送具体消息失败！");
    log.info("用户注册成功，现在发送message，给user-account ! ");

  }

}
