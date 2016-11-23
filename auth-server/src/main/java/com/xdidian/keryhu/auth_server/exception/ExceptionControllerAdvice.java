/**
 * @Title: ExceptionControllerAdvice.java
 * @Package com.xdidian.keryhu.useraccount.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年4月30日 上午10:33:44
 * @version V1.0
 */
package com.xdidian.keryhu.auth_server.exception;

import com.xdidian.keryhu.domain.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 
 * @Description : 用户登录成功后，发送userId出去，发送失败
 * @date : 2016年6月18日 下午7:57:01
 * @author : keryHu keryhu@hotmail.com
 */
@ControllerAdvice
public class ExceptionControllerAdvice {


  // 捕获Assert验证的错误信息。
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  ErrorMessage handleIllegalArgumentException(Throwable ex) {

    // 每次定义错误的时候，需要手动加上错误的HttpStatus.NOT_FOUND.value() 的类型
    return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }



}
