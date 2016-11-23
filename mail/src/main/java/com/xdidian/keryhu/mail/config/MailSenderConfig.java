package com.xdidian.keryhu.mail.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.xdidian.keryhu.mail.domain.EmailStatus;

import javax.mail.internet.MimeMessage;


/**
 * @Description : 创建一个发送email 激活的 邮件 sender 方法
 * @date : 2016年6月18日 下午9:04:39
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MailSenderConfig {

  private final JavaMailSender javaMailSender;


  /**
   * 当发送邮件的内容，只是纯文本的时候。
   */
  public EmailStatus sendPlainText(String to, String subject, String text) {
    return sendM(to, subject, text, false);
  }

  /**
   *
   * 发送内容为html格式的时候。
   */
  public EmailStatus sendHtml(String to, String subject, String htmlBody) {
    return sendM(to, subject, htmlBody, true);
  }


  private EmailStatus sendM(String to, String subject, String text, Boolean isHtml) {
    try {
      MimeMessage mail = javaMailSender.createMimeMessage();
      // 同事设置了附件功能
      MimeMessageHelper helper = new MimeMessageHelper(mail, true /* multipart */, "UTF-8");
      helper.setFrom("948747450@qq.com");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text, isHtml);
      javaMailSender.send(mail);
      log.info("Send email '{}' to: {}", subject, to);
      return new EmailStatus(to, subject, text).success();
    } catch (Exception e) {
      log.error(String.format("Problem with sending email to: {}, error message: {}", to,
          e.getMessage()));
      return new EmailStatus(to, subject, text).error(e.getMessage());
    }
  }

}
