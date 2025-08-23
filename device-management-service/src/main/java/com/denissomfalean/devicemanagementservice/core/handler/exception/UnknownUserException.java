package com.denissomfalean.devicemanagementservice.core.handler.exception;

public class UnknownUserException extends RuntimeException {
  public UnknownUserException(String message) {
    super(message);
  }
}
