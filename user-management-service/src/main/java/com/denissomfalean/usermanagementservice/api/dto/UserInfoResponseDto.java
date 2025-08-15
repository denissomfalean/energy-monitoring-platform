package com.denissomfalean.usermanagementservice.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
  private Long id;
  private String username;
  private String password;
  private AccessRoleDto role;
}
