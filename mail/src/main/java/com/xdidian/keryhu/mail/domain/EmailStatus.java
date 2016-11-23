package com.xdidian.keryhu.mail.domain;



/**
 * : 邮件发送的主要参数状态
 *  : 2016年6月18日 下午9:04:56
 * @author : keryHu keryhu@hotmail.com
 */
public class EmailStatus {

  public static final String SUCCESS = "SUCCESS";
  public static final String ERROR = "ERROR";

  private final String to;
  private final String subject;
  private final String body;

  private String status;
  private String errorMessage;

  public EmailStatus(String to, String subject, String body) {
    this.to = to;
    this.subject = subject;
    this.body = body;
  }

  public EmailStatus success() {
    this.status = SUCCESS;
    return this;
  }

  public EmailStatus error(String errorMessage) {
    this.status = ERROR;
    this.errorMessage = errorMessage;
    return this;
  }

  public boolean isSuccess() {
    return SUCCESS.equals(this.status);
  }

  public boolean isError() {
    return ERROR.equals(this.status);
  }

  public String getTo() {
    return to;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public String getStatus() {
    return status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

}
