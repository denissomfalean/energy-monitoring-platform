package com.denissomfalean.usermanagementservice.core.mapper;

import com.denissomfalean.usermanagementservice.api.dto.UserInfoResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserSaveRequestDto;
import com.denissomfalean.usermanagementservice.core.persistence.entity.AccessRole;
import com.denissomfalean.usermanagementservice.core.persistence.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
  public static UserInfoResponseDto toUserInfoResponseDto(UserEntity userEntity) {

    return UserInfoResponseDto.builder()
        .id(userEntity.getId())
        .username(userEntity.getUsername())
        .password(userEntity.getPassword())
        .role(userEntity.getAccessRole().getDto())
        .build();
  }

  public static UserEntity toUserEntity(UserSaveRequestDto userSaveRequestDto) {
    return UserEntity.builder()
        .username(userSaveRequestDto.getUsername())
        .password(userSaveRequestDto.getPassword())
        .accessRole(AccessRole.fromDto(userSaveRequestDto.getAccessRole()))
        .build();
  }
}
