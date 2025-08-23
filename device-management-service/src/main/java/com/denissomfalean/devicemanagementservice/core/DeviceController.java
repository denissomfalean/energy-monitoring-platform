package com.denissomfalean.devicemanagementservice.core;

import com.denissomfalean.devicemanagementservice.api.DeviceApi;
import com.denissomfalean.devicemanagementservice.api.dto.DeviceInfoResponseDto;
import com.denissomfalean.devicemanagementservice.api.dto.DeviceSaveRequestDto;
import com.denissomfalean.devicemanagementservice.core.handler.GlobalExceptionHandler;
import com.denissomfalean.devicemanagementservice.core.service.DeviceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeviceController extends GlobalExceptionHandler implements DeviceApi {
  private final DeviceService deviceService;

  @Override
  public ResponseEntity<List<DeviceInfoResponseDto>> getAllDevices() {
    return ResponseEntity.ok(deviceService.getAllDevices());
  }

  @Override
  public ResponseEntity<List<DeviceInfoResponseDto>> getAllDevicesByUserId(Long userId) {
    return ResponseEntity.ok(deviceService.getAllDevicesByUserId(userId));
  }

  @Override
  public ResponseEntity<DeviceInfoResponseDto> save(DeviceSaveRequestDto deviceSaveRequestDto) {
    return new ResponseEntity<>(deviceService.save(deviceSaveRequestDto), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<DeviceInfoResponseDto> updateDeviceById(
      Long id, DeviceSaveRequestDto deviceSaveRequestDto) {
    return ResponseEntity.ok(deviceService.updateDeviceById(id, deviceSaveRequestDto));
  }

  @Override
  public ResponseEntity<HttpStatus> delete(Long id) {
    deviceService.delete(id);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
