package com.denissomfalean.usermanagementservice.core.handler.exception;

public class UserBadRequestException extends RuntimeException {
  public UserBadRequestException(String message) {
    super(message);
  }
}
