package me.catzy.prestiz.exceptions;

public class AuthenticationException extends Exception implements ExceptionWithHttpCode {
  private static final long serialVersionUID = -586399601531313928L;
  
  public AuthenticationException(String message) {
    super(message);
  }
  
  public int getHttpCode() {
    return 401;
  }
}
