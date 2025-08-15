package com.denissomfalean.usermanagementservice.core.controller;

import com.denissomfalean.usermanagementservice.api.UserApi;
import com.denissomfalean.usermanagementservice.api.dto.UserInfoResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserSaveRequestDto;
import com.denissomfalean.usermanagementservice.core.handler.GlobalExceptionHandler;
import com.denissomfalean.usermanagementservice.core.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends GlobalExceptionHandler implements UserApi {
  private final UserService userService;

  @Override
  public ResponseEntity<List<UserInfoResponseDto>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Override
  public ResponseEntity<UserInfoResponseDto> getUserById(Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Override
  public ResponseEntity<UserInfoResponseDto> save(UserSaveRequestDto userSaveRequestDto) {
    return new ResponseEntity<>(userService.save(userSaveRequestDto), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<UserInfoResponseDto> updateUserById(
      Long id, UserSaveRequestDto userSaveRequestDto) {
    return ResponseEntity.ok(userService.updateUserById(id, userSaveRequestDto));
  }

  @Override
  public ResponseEntity<HttpStatus> delete(Long id) {
    userService.delete(id);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
