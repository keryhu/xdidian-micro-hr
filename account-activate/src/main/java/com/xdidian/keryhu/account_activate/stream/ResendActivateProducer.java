package com.xdidian.keryhu.account_activate.stream;

import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * @Description : TODO(此方法是三合一的方法，包含了用户前台个人资料的修改，密码重设，账号注册完的邮箱验证  的 重复发送的 message
 * 接受此消息的有2个服务。
 * 1 邮件服务器 ，接受此判断，account是否为email，如果是email，那么邮件服务器就处理，发送相应的邮件
 * 2 手机平台，，接受此判断，account是否为手机，如果是手机，那么手机平台就发送相应的短信)
 * 
 * －－－－－
 * 此消息接口，还提供了一个通道的服务，就是用户密码重设的时候，从这里发送消息出去，
 * 接受者是： 邮件服务器和手机平台，逻辑判断和上面的一样。
 * 
 * @date : 2016年6月18日 下午9:02:58
 * @author : keryHu keryhu@hotmail.com
 */


@Component
@EnableBinding(ResendActivateOutputChannel.class)
@Slf4j
public class ResendActivateProducer {

  @Autowired
  private ResendActivateOutputChannel channel;

  public void send(CommonTokenDto dto) {

    boolean result = channel.resend().send(MessageBuilder.withPayload(dto).build());

    Assert.isTrue(result, "用户用户点击'再次发送邮件激活的需求，再次发送的请求message发出失败！");

    log.info("用户用户点击'再次发送邮件激活的需求，再次发送的请求message发出！发送email token is： ' {} ",dto.getToken());
  }


}
