package com.denissomfalean.usermanagementservice.api;

import com.denissomfalean.usermanagementservice.api.dto.UserInfoResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserSaveRequestDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface UserApi {
  String USER_MANAGEMENT_API_URL = "/user-management";

  @GetMapping(USER_MANAGEMENT_API_URL)
  ResponseEntity<List<UserInfoResponseDto>> getAllUsers();

  @GetMapping(USER_MANAGEMENT_API_URL + "/{id}")
  ResponseEntity<UserInfoResponseDto> getUserById(@PathVariable("id") Long id);

  @PostMapping(USER_MANAGEMENT_API_URL)
  ResponseEntity<UserInfoResponseDto> save(@RequestBody UserSaveRequestDto userSaveRequestDto);

  @PutMapping(USER_MANAGEMENT_API_URL + "/{id}")
  ResponseEntity<UserInfoResponseDto> updateUserById(
      @PathVariable("id") Long id, @RequestBody UserSaveRequestDto userSaveRequestDto);

  @DeleteMapping(USER_MANAGEMENT_API_URL + "/{id}")
  ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id);
}
