package com.xdidian.keryhu.account_activate.exception;

public class TokenNotFoundException extends RuntimeException{

  private static final long serialVersionUID = -7083945751893389129L;
  
  public TokenNotFoundException(String m){
    super(m);
  }

}
