package com.xdidian.keryhu.account_activate.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;



/**
 * @Description : TODO(此方法是三合一的方法，包含了用户前台个人资料的修改，密码重设，账号注册完的邮箱验证  的 重复发送的 message
 * 接受此消息的有2个服务。
 * 1 邮件服务器 ，接受此判断，account是否为email，如果是email，那么邮件服务器就处理，发送相应的邮件
 * 2 手机平台，，接受此判断，account是否为手机，如果是手机，那么手机平台就发送相应的短信)
 * @date : 2016年6月18日 下午9:02:58
 * @author : keryHu keryhu@hotmail.com
 */
public interface ResendActivateOutputChannel {

  // 此channel的值和 application bindings下面的值一致
  String NAME = "resendActivateOutputChannel";

  @Output(NAME)
  MessageChannel resend();

}
