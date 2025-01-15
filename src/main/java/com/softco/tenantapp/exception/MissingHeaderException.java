package com.softco.tenantapp.exception;

public class MissingHeaderException extends RuntimeException {

  public MissingHeaderException(String message) {
    super(message);
  }

  public MissingHeaderException(String message, Throwable cause) {
    super(message, cause);
  }

}
