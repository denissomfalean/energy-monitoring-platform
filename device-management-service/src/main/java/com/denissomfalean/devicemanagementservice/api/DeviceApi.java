package com.denissomfalean.devicemanagementservice.api;

import com.denissomfalean.devicemanagementservice.api.dto.DeviceInfoResponseDto;
import com.denissomfalean.devicemanagementservice.api.dto.DeviceSaveRequestDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

public interface DeviceApi {
  String DEVICE_MANAGEMENT_API_URL = "/device-management";

  @GetMapping(DEVICE_MANAGEMENT_API_URL)
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  ResponseEntity<List<DeviceInfoResponseDto>> getAllDevices();

  @GetMapping(DEVICE_MANAGEMENT_API_URL + "/userId/{userId}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
  ResponseEntity<List<DeviceInfoResponseDto>> getAllDevicesByUserId(
      @PathVariable("userId") Long userId);

  @PostMapping(DEVICE_MANAGEMENT_API_URL)
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  ResponseEntity<DeviceInfoResponseDto> save(
      @RequestBody DeviceSaveRequestDto deviceSaveRequestDto);

  @PutMapping(DEVICE_MANAGEMENT_API_URL + "/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  ResponseEntity<DeviceInfoResponseDto> updateDeviceById(
      @PathVariable("id") Long id, @RequestBody DeviceSaveRequestDto deviceSaveRequestDto);

  @DeleteMapping(DEVICE_MANAGEMENT_API_URL + "/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id);
}
