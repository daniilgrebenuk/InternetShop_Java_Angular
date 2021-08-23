package com.project.internetshop.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthorizationException extends AuthenticationException {
  private final HttpStatus httpStatus;

  public JwtAuthorizationException(String msg, HttpStatus httpStatus) {
    super(msg);
    this.httpStatus = httpStatus;
  }


}
