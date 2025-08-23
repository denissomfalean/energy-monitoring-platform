package com.denissomfalean.devicemanagementservice.core.handler.exception;

public class DeviceBadRequestException extends RuntimeException {
  public DeviceBadRequestException(String message) {
    super(message);
  }
}
