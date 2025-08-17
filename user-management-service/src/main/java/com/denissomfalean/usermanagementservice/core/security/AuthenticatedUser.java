package com.denissomfalean.usermanagementservice.core.security;

import static java.util.Arrays.stream;

import com.denissomfalean.usermanagementservice.core.persistence.entity.UserEntity;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class AuthenticatedUser implements UserDetails {
  private final UserEntity user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String[] claims = {this.user.getAccessRole().name()};
    return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }
}
