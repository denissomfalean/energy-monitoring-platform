package com.denissomfalean.devicemanagementservice.core.mapper;

import com.denissomfalean.devicemanagementservice.api.dto.DeviceInfoResponseDto;
import com.denissomfalean.devicemanagementservice.api.dto.DeviceSaveRequestDto;
import com.denissomfalean.devicemanagementservice.core.persistence.entity.DeviceEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeviceMapper {
  public DeviceInfoResponseDto toDeviceInfoResponseDto(DeviceEntity deviceEntity) {

    return DeviceInfoResponseDto.builder()
        .id(deviceEntity.getId())
        .userId(deviceEntity.getUserId())
        .description(deviceEntity.getDescription())
        .address(deviceEntity.getAddress())
        .maximumHourlyEnergyConsumption(deviceEntity.getMaximumHourlyEnergyConsumption())
        .build();
  }

  public DeviceEntity toDeviceEntity(DeviceSaveRequestDto deviceSaveRequestDto) {

    return DeviceEntity.builder()
        .userId(deviceSaveRequestDto.getUserId())
        .description(deviceSaveRequestDto.getDescription())
        .address(deviceSaveRequestDto.getAddress())
        .maximumHourlyEnergyConsumption(deviceSaveRequestDto.getMaximumHourlyEnergyConsumption())
        .build();
  }
}
