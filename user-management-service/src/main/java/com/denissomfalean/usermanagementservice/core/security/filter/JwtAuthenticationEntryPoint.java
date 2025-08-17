package com.denissomfalean.usermanagementservice.core.security.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.denissomfalean.usermanagementservice.core.handler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {
  private static final String FORBIDDEN_MESSAGE = "You need to login to access this page";

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    ErrorResponse httpResponse =
        new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN,
            HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(),
            FORBIDDEN_MESSAGE);
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    OutputStream outputStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(outputStream, httpResponse);
    outputStream.flush();
  }
}
