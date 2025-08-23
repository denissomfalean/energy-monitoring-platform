package com.denissomfalean.devicemanagementservice.core.service;

import com.denissomfalean.devicemanagementservice.api.dto.DeviceInfoResponseDto;
import com.denissomfalean.devicemanagementservice.api.dto.DeviceSaveRequestDto;
import java.util.List;

public interface DeviceService {
  List<DeviceInfoResponseDto> getAllDevices();

  List<DeviceInfoResponseDto> getAllDevicesByUserId(Long userId);

  DeviceInfoResponseDto save(DeviceSaveRequestDto deviceSaveRequestDto);

  DeviceInfoResponseDto updateDeviceById(Long id, DeviceSaveRequestDto deviceSaveRequestDto);

  void delete(Long id);
}
