package com.softco.tenantapp.exception;

public class TenantNotFoundException extends RuntimeException {

  public TenantNotFoundException(String message) {
    super(message);
  }

  public TenantNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
