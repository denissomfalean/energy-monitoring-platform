package com.denissomfalean.devicemanagementservice.core.security;

import com.denissomfalean.devicemanagementservice.core.handler.exception.AccessRoleException;

public enum AccessRole {
  ADMIN,
  CLIENT;

  private static final String COULD_NOT_IDENTIFY_ROLE =
      "Could not convert from %s to a known access role";

  public static AccessRole fromString(String role) {
    for (AccessRole accessRole : values()) {
      if (accessRole.name().equalsIgnoreCase(role)) {
        return accessRole;
      }
    }
    throw new AccessRoleException(String.format(COULD_NOT_IDENTIFY_ROLE, role));
  }
}
