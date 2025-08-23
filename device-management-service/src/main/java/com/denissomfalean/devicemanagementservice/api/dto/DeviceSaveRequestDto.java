package com.denissomfalean.devicemanagementservice.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSaveRequestDto {
  private Long userId;
  private String description;
  private String address;
  private int maximumHourlyEnergyConsumption;
}
