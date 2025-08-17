package com.denissomfalean.usermanagementservice.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {
  private String username;
  private String password;
}
