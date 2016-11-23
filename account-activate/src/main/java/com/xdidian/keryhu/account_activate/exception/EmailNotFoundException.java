package com.xdidian.keryhu.account_activate.exception;

public class EmailNotFoundException extends RuntimeException{

  private static final long serialVersionUID = -4084668795444462031L;

  public EmailNotFoundException(String m){
    super(m);
  }
}
