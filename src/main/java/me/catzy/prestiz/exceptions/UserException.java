package me.catzy.prestiz.exceptions;

public class UserException extends Exception implements ExceptionWithHttpCode {
  private static final long serialVersionUID = -586399601531313928L;
  
  private String userMessage;
  
  public UserException(String message, String userMessage) {
    super(message);
    this.userMessage = userMessage;
  }
  
  public int getHttpCode() {
    return 400;
  }
  
  public String getUserMessage() {
    return this.userMessage;
  }
}
