package com.denissomfalean.usermanagementservice.core.service;

import com.denissomfalean.usermanagementservice.api.dto.UserInfoResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserLoginResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserSaveRequestDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface UserService {
  UserLoginResponseDto login(Authentication authentication);

  List<UserInfoResponseDto> getAllUsers();

  UserInfoResponseDto getUserById(Long id);

  UserInfoResponseDto save(UserSaveRequestDto userSaveRequestDto);

  UserInfoResponseDto updateUserById(Long id, UserSaveRequestDto userSaveRequestDto);

  void delete(Long id);
}
