package com.denissomfalean.devicemanagementservice.core.security.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.denissomfalean.devicemanagementservice.core.security.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private static final String TOKEN_PREFIX = "Bearer ";
  private static final String OPTIONS_HTTP_METHOD = "OPTIONS";

  private final JwtProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
      response.setStatus(HttpStatus.OK.value());
    } else {
      String authorizationHeader = request.getHeader(AUTHORIZATION);
      if ((authorizationHeader == null) || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
        filterChain.doFilter(request, response);
        return;
      }
      String token = authorizationHeader.substring(TOKEN_PREFIX.length());
      String userId = jwtTokenProvider.getSubject(token);
      if (jwtTokenProvider.isTokenValid(userId, token)
          && SecurityContextHolder.getContext().getAuthentication() == null) {
        List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);

        Authentication authentication =
            jwtTokenProvider.getAuthentication(userId, authorities, request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        SecurityContextHolder.clearContext();
      }
    }
    filterChain.doFilter(request, response);
  }
}
