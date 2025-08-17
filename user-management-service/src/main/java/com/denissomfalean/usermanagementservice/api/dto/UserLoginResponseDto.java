package com.denissomfalean.usermanagementservice.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {
  private UserInfoResponseDto userInfo;
  private String jwt;
}
