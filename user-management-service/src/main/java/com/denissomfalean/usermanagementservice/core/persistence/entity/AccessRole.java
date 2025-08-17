package com.denissomfalean.usermanagementservice.core.persistence.entity;

import com.denissomfalean.usermanagementservice.api.dto.AccessRoleDto;
import com.denissomfalean.usermanagementservice.core.handler.exception.AccessRoleException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccessRole {
  ADMIN(AccessRoleDto.ADMIN),
  CLIENT(AccessRoleDto.CLIENT);

  private final AccessRoleDto dto;

  private static final String COULD_NOT_IDENTIFY_ROLE =
      "Could not convert from %s to a known access role";

  public static AccessRole fromDto(AccessRoleDto dto) {
    for (AccessRole role : values()) {
      if (role.dto == dto) {
        return role;
      }
    }
    throw new AccessRoleException(String.format(COULD_NOT_IDENTIFY_ROLE, dto.name()));
  }

  public static AccessRole fromString(String role) {
    for (AccessRole accessRole : values()) {
      if (accessRole.name().equalsIgnoreCase(role)) {
        return accessRole;
      }
    }
    throw new AccessRoleException(String.format(COULD_NOT_IDENTIFY_ROLE, role));
  }
}
