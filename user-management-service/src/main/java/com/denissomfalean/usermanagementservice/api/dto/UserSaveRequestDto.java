package com.denissomfalean.usermanagementservice.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequestDto {
  private String username;
  private String password;
  private AccessRoleDto accessRole;
}
