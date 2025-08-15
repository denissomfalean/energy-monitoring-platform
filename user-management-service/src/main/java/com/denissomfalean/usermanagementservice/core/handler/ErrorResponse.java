package com.denissomfalean.usermanagementservice.core.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
  @Temporal(TemporalType.DATE)
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "MM-dd-yyyy hh:mm:ss",
      locale = "pt-EU",
      timezone = "Europe/Bucharest")
  private Date timeStamp;

  private int httpStatusCode;
  private HttpStatus httpStatus;
  private String reason;
  private String message;

  public ErrorResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
    this.timeStamp = new Date();
    this.httpStatusCode = httpStatusCode;
    this.httpStatus = httpStatus;
    this.reason = reason;
    this.message = message;
  }

  public static ResponseEntity<ErrorResponse> createErrorResponse(
      HttpStatus httpStatus, String message) {
    ErrorResponse errorResponse =
        new ErrorResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(), message);
    return new ResponseEntity<>(errorResponse, httpStatus);
  }
}
