package com.denissomfalean.devicemanagementservice.core.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "devices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private int maximumHourlyEnergyConsumption;
}
