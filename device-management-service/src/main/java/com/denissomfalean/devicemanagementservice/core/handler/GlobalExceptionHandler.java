package com.denissomfalean.devicemanagementservice.core.handler;

import static com.denissomfalean.devicemanagementservice.core.handler.ErrorResponse.createErrorResponse;

import com.denissomfalean.devicemanagementservice.core.handler.exception.AccessRoleException;
import com.denissomfalean.devicemanagementservice.core.handler.exception.DeviceBadRequestException;
import com.denissomfalean.devicemanagementservice.core.handler.exception.UnknownUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(AccessRoleException.class)
  public ResponseEntity<ErrorResponse> accessRoleException(AccessRoleException exception) {
    return createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(UnknownUserException.class)
  public ResponseEntity<ErrorResponse> unknownUserException(UnknownUserException exception) {
    return createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(DeviceBadRequestException.class)
  public ResponseEntity<ErrorResponse> deviceBadRequestException(
      DeviceBadRequestException exception) {
    return createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
  }
}
