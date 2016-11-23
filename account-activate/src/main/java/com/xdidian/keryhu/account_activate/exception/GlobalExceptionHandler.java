package com.xdidian.keryhu.account_activate.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.xdidian.keryhu.domain.ErrorMessage;

import org.springframework.http.HttpStatus;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

  // 捕获Assert验证的错误信息。
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  ErrorMessage handleIllegalArgumentException(Throwable e) {

    // 每次定义错误的时候，需要手动加上错误的HttpStatus.NOT_FOUND.value() 的类型
    return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()) ;
  }

  // 捕获email 找不到。
  @ExceptionHandler(EmailNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorMessage handleEmailNotFoundException(Throwable e) {

    // 每次定义错误的时候，需要手动加上错误的HttpStatus.NOT_FOUND.value() 的类型
    return new ErrorMessage(HttpStatus.NOT_FOUND.value(),e.getMessage()) ;
  }

 


  // 捕获token 找不到。
  @ExceptionHandler(TokenNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorMessage handleTokenNotFoundException(Throwable e) {

    // 每次定义错误的时候，需要手动加上错误的HttpStatus.NOT_FOUND.value() 的类型
    return new ErrorMessage(HttpStatus.NOT_FOUND.value(),e.getMessage()) ;
  }


}
