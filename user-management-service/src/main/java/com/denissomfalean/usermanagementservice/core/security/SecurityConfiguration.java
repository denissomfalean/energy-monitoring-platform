package com.denissomfalean.usermanagementservice.core.security;

import com.denissomfalean.usermanagementservice.core.handler.exception.UserBadRequestException;
import com.denissomfalean.usermanagementservice.core.persistence.UserRepository;
import com.denissomfalean.usermanagementservice.core.security.filter.JwtAccessDeniedHandler;
import com.denissomfalean.usermanagementservice.core.security.filter.JwtAuthenticationEntryPoint;
import com.denissomfalean.usermanagementservice.core.security.filter.JwtAuthorizationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private static final String[] PUBLIC_URLS = {"/user-management/login"};
  private static final List<String> ALLOWED_URLS_FOR_ACCESS = List.of("http://localhost:4200");
  private static final List<String> ALLOWED_HEADERS = List.of("*");
  private static final List<String> ALLOWED_METHODS =
      List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
  private static final String USER_NOT_FOUND = "User not found";

  private final JwtAuthorizationFilter jwtAuthorizationFilter;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final UserRepository userRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return username ->
        new AuthenticatedUser(
            userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserBadRequestException(USER_NOT_FOUND)));
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            request ->
                request.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
        .exceptionHandling(
            exception ->
                exception
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler))
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(ALLOWED_URLS_FOR_ACCESS);
    configuration.setAllowedMethods(ALLOWED_METHODS);
    configuration.setAllowedHeaders(ALLOWED_HEADERS);
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
