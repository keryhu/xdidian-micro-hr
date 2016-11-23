package com.xdidian.keryhu.user.exception;


/**
 * @Description : 当根据identity查询数据库是，查询不到对应的user的时候，报此错误
 * @date : 2016年6月18日 下午9:20:35
 * @author : keryHu keryhu@hotmail.com
 */
public class UserNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 8378668714759997552L;

  public UserNotFoundException(String message) {
    super(message);
  }
}
