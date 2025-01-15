package com.softco.tenantapp.exception;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

  private String message;
  private LocalDateTime date;
  private String cause;

  public ErrorResponse(String message, String cause) {
    this.message = message;
    this.cause = cause;
    this.date = LocalDateTime.now();
  }
}
