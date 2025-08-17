package com.denissomfalean.usermanagementservice.core.security;

import static java.util.Arrays.stream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private static final String ISSUER = "EMP";
  private static final String ADMINISTRATION = "User Management Service";
  private static final String AUTHORITIES = "Authorities";
  private static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";

  /** 5 days in milliseconds. */
  private static final long EXPIRATION_TIME = 432_000_000;

  @Value("${jwt.secret}")
  private String secret;

  public String generateJwtToken(AuthenticatedUser authenticatedUser) {
    String[] claims = getClaimsFromUser(authenticatedUser.getAuthorities());
    return JWT.create()
        .withIssuer(ISSUER)
        .withAudience(ADMINISTRATION)
        .withIssuedAt(new Date())
        .withSubject(String.valueOf(authenticatedUser.getUser().getId()))
        .withArrayClaim(AUTHORITIES, claims)
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(secret.getBytes()));
  }

  public List<GrantedAuthority> getAuthorities(String token) {
    String[] claims = getClaimsFromToken(token);
    return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  public Authentication getAuthentication(
      String userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(userId, null, authorities);
    usernamePasswordAuthenticationToken.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request));
    return usernamePasswordAuthenticationToken;
  }

  public boolean isTokenValid(String userId, String token) {
    JWTVerifier verifier = getJWTVerifier();
    return StringUtils.isNotEmpty(userId) && !isTokenExpired(verifier, token);
  }

  public String getSubject(String token) {
    JWTVerifier verifier = getJWTVerifier();
    return verifier.verify(token).getSubject();
  }

  private boolean isTokenExpired(JWTVerifier verifier, String token) {
    Date expiration = verifier.verify(token).getExpiresAt();
    return expiration.before(new Date());
  }

  private String[] getClaimsFromUser(Collection<? extends GrantedAuthority> grantedAuthorities) {
    List<String> authorities = new ArrayList<>();
    for (GrantedAuthority grantedAuthority : grantedAuthorities) {
      authorities.add(grantedAuthority.getAuthority());
    }

    return authorities.toArray(new String[0]);
  }

  private String[] getClaimsFromToken(String token) {
    JWTVerifier verifier = getJWTVerifier();
    return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
  }

  private JWTVerifier getJWTVerifier() {
    JWTVerifier verifier;
    try {
      Algorithm algorithm = Algorithm.HMAC512(secret);
      verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
    } catch (JWTVerificationException e) {
      throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
    }
    return verifier;
  }
}
