package com.xdidian.keryhu.user.exception;

/**
 * @Description : email找不到的错误
 * @date : 2016年6月18日 下午9:20:01
 * @author : keryHu keryhu@hotmail.com
 */
public class EmailNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4150301770264026174L;

  public EmailNotFoundException(String message) {
    super(message);
  }

}
