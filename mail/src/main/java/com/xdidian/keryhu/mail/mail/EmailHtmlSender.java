package com.xdidian.keryhu.mail.mail;

import com.xdidian.keryhu.mail.config.MailSenderConfig;
import com.xdidian.keryhu.mail.domain.EmailStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



/**
 *  : thymeleaf html格式的 spring mail配置
 *  : 2016年6月18日 下午9:05:32
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailHtmlSender {

  private final MailSenderConfig emailSender;

  private final TemplateEngine templateEngine;

  /**
   * 发送邮件的主程序
   *
   * @param context 发送的内容上下文
   * @param subject 发送的主题
   * @param templateName 发送的template模版名字
   * @param to 发送的对象,email地址
   */
  public EmailStatus send(String to, String subject, String templateName, Context context) {
    String body = templateEngine.process(templateName, context);
    return emailSender.sendHtml(to, subject, body);
  }


}
