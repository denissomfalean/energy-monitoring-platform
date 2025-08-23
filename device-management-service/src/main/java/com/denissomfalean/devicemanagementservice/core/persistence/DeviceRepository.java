package com.denissomfalean.devicemanagementservice.core.persistence;

import com.denissomfalean.devicemanagementservice.core.persistence.entity.DeviceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
  List<DeviceEntity> findAllByUserId(Long userId);
}
