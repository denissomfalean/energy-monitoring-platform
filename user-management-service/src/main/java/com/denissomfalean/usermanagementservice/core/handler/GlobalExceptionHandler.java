package com.denissomfalean.usermanagementservice.core.handler;

import static com.denissomfalean.usermanagementservice.core.handler.ErrorResponse.createErrorResponse;

import com.denissomfalean.usermanagementservice.core.handler.exception.AccessRoleException;
import com.denissomfalean.usermanagementservice.core.handler.exception.UserBadRequestException;
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

  @ExceptionHandler(UserBadRequestException.class)
  public ResponseEntity<ErrorResponse> userBadRequestException(UserBadRequestException exception) {
    return createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
  }
}
