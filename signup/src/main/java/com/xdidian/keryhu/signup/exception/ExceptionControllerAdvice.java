package com.xdidian.keryhu.signup.exception;

import com.xdidian.keryhu.domain.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



/**
 * @Description : 物业公司注册过程中产生的exception 的 controller、 处理
 * @date : 2016年6月18日 下午9:15:15
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
