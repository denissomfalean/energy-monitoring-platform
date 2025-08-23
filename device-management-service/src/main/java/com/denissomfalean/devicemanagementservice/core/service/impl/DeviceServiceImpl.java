package com.denissomfalean.devicemanagementservice.core.service.impl;

import com.denissomfalean.devicemanagementservice.api.dto.DeviceInfoResponseDto;
import com.denissomfalean.devicemanagementservice.api.dto.DeviceSaveRequestDto;
import com.denissomfalean.devicemanagementservice.core.handler.exception.DeviceBadRequestException;
import com.denissomfalean.devicemanagementservice.core.mapper.DeviceMapper;
import com.denissomfalean.devicemanagementservice.core.persistence.DeviceRepository;
import com.denissomfalean.devicemanagementservice.core.persistence.entity.DeviceEntity;
import com.denissomfalean.devicemanagementservice.core.security.AccessRole;
import com.denissomfalean.devicemanagementservice.core.service.DeviceService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
  private static final String NO_DEVICES_COULD_BE_FOUND = "No devices could be found";
  private static final String NO_DEVICES_FOUND_FOR_USER =
      "No devices could be found for the given user";
  private static final String ACCESS_DENIED_MESSAGE =
      "You do not have permission to access this page";
  public static final String AUTHENTICATED_USER_HAS_NO_ROLE_ASSIGNED =
      "Authenticated user has no role assigned";
  public static final String NO_DEVICES_FOUND_FOR_THE_USER_ID =
      "No devices found for the user id: {{}}";
  public static final String NO_DEVICES_FOUND_FOR_ID = "No devices found for the given id: {{}}";

  private final DeviceRepository deviceRepository;

  @Override
  public List<DeviceInfoResponseDto> getAllDevices() {
    List<DeviceEntity> listOfDevices = deviceRepository.findAll();

    if (listOfDevices.isEmpty()) {
      log.error(NO_DEVICES_COULD_BE_FOUND);
      throw new DeviceBadRequestException(NO_DEVICES_COULD_BE_FOUND);
    }

    return listOfDevices.stream()
        .map(DeviceMapper::toDeviceInfoResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<DeviceInfoResponseDto> getAllDevicesByUserId(Long userId) {
    checkUserIdAccessRights(userId);
    List<DeviceEntity> listOfDevices = deviceRepository.findAllByUserId(userId);

    if (listOfDevices.isEmpty()) {
      log.error(NO_DEVICES_FOUND_FOR_THE_USER_ID, userId);
      throw new DeviceBadRequestException(NO_DEVICES_FOUND_FOR_USER);
    }

    return listOfDevices.stream()
        .map(DeviceMapper::toDeviceInfoResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public DeviceInfoResponseDto save(DeviceSaveRequestDto deviceSaveRequestDto) {
    checkIfUserExists(deviceSaveRequestDto.getUserId());

    DeviceEntity deviceEntity = DeviceMapper.toDeviceEntity(deviceSaveRequestDto);

    return DeviceMapper.toDeviceInfoResponseDto(deviceRepository.save(deviceEntity));
  }

  @Override
  public DeviceInfoResponseDto updateDeviceById(
      Long id, DeviceSaveRequestDto deviceSaveRequestDto) {
    checkIfUserExists(deviceSaveRequestDto.getUserId());
    DeviceEntity existingDeviceEntity = getDeviceEntityById(id);

    DeviceEntity deviceEntityToUpdate = DeviceMapper.toDeviceEntity(deviceSaveRequestDto);
    deviceEntityToUpdate.setId(existingDeviceEntity.getId());

    return DeviceMapper.toDeviceInfoResponseDto(deviceRepository.save(deviceEntityToUpdate));
  }

  @Override
  public void delete(Long id) {
    DeviceEntity deviceEntity = getDeviceEntityById(id);

    deviceRepository.delete(deviceEntity);
  }

  private void checkUserIdAccessRights(Long userId) {
    Long loggedUserId = getCurrentUserId();
    AccessRole accessRole = getCurrentUserRole();

    if (AccessRole.CLIENT.equals(accessRole) && !(loggedUserId.equals(userId))) {
      throw new DeviceBadRequestException(ACCESS_DENIED_MESSAGE);
    }
  }

  private AccessRole getCurrentUserRole() {
    String accessRoleAsString =
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElseThrow(
                () -> new DeviceBadRequestException(AUTHENTICATED_USER_HAS_NO_ROLE_ASSIGNED));

    return AccessRole.fromString(accessRoleAsString);
  }

  private Long getCurrentUserId() {
    String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return Long.parseLong(userId);
  }

  private DeviceEntity getDeviceEntityById(Long id) {
    Optional<DeviceEntity> optionalDeviceEntity = deviceRepository.findById(id);

    if (optionalDeviceEntity.isEmpty()) {
      log.error(NO_DEVICES_FOUND_FOR_ID, id);
      throw new DeviceBadRequestException(NO_DEVICES_FOUND_FOR_USER);
    }

    return optionalDeviceEntity.get();
  }

  // TODO: To implement with call to user-management-service through Feign Client
  private void checkIfUserExists(Long userId) {}
}
