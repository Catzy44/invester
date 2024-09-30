package me.catzy.prestiz.exceptions;

public class DataInvalidException extends Exception implements ExceptionWithHttpCode {
  private static final long serialVersionUID = -586399601531313928L;
  
  public DataInvalidException(String message) {
    super(message);
  }
  
  public int getHttpCode() {
    return 488;
  }
}
