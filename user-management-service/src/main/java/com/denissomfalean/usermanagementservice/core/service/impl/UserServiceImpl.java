package com.denissomfalean.usermanagementservice.core.service.impl;

import com.denissomfalean.usermanagementservice.api.dto.UserInfoResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserSaveRequestDto;
import com.denissomfalean.usermanagementservice.core.handler.exception.UserBadRequestException;
import com.denissomfalean.usermanagementservice.core.mapper.UserMapper;
import com.denissomfalean.usermanagementservice.core.persistence.UserRepository;
import com.denissomfalean.usermanagementservice.core.persistence.entity.UserEntity;
import com.denissomfalean.usermanagementservice.core.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private static final String NO_USERS_COULD_BE_FOUND = "No users could be found";
  private static final String PROVIDED_USERNAME_CANNOT_BE_USED = "Provided username cannot be used";
  private static final String CANNOT_UPDATE_USER_DATA = "Cannot update user data";
  public static final String NO_USER_COULD_BE_FOUND_FOR_ID = "No user could be found for id {{}}";
  public static final String THE_PROVIDED_USERNAME_WAS_NOT_UNIQUE =
      "The provided username was not unique: {{}}";

  private final UserRepository userRepository;

  @Override
  public List<UserInfoResponseDto> getAllUsers() {
    List<UserEntity> listOfUsers = userRepository.findAll();

    if (listOfUsers.isEmpty()) {
      log.error(NO_USERS_COULD_BE_FOUND);
      throw new UserBadRequestException(NO_USERS_COULD_BE_FOUND);
    }

    return listOfUsers.stream().map(UserMapper::toUserInfoResponseDto).collect(Collectors.toList());
  }

  @Override
  public UserInfoResponseDto getUserById(Long id) {
    UserEntity userEntity = getUserEntityById(id);

    return UserMapper.toUserInfoResponseDto(userEntity);
  }

  @Override
  public UserInfoResponseDto save(UserSaveRequestDto userSaveRequestDto) {
    validateUsername(userSaveRequestDto.getUsername());

    UserEntity userEntity = UserMapper.toUserEntity(userSaveRequestDto);

    return UserMapper.toUserInfoResponseDto(userRepository.save(userEntity));
  }

  @Override
  public UserInfoResponseDto updateUserById(Long id, UserSaveRequestDto userSaveRequestDto) {
    validateUsername(userSaveRequestDto.getUsername());
    UserEntity existingUserEntity = getUserEntityById(id);

    UserEntity userEntityToUpdate = UserMapper.toUserEntity(userSaveRequestDto);
    userEntityToUpdate.setId(existingUserEntity.getId());

    return UserMapper.toUserInfoResponseDto(userRepository.save(userEntityToUpdate));
  }

  @Override
  public void delete(Long id) {
    UserEntity userEntity = getUserEntityById(id);

    userRepository.delete(userEntity);
  }

  private UserEntity getUserEntityById(Long id) {
    Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

    if (optionalUserEntity.isEmpty()) {
      log.error(NO_USER_COULD_BE_FOUND_FOR_ID, id);
      throw new UserBadRequestException(CANNOT_UPDATE_USER_DATA);
    }

    return optionalUserEntity.get();
  }

  private void validateUsername(String username) {
    if (StringUtils.isBlank(username)) {
      log.error(PROVIDED_USERNAME_CANNOT_BE_USED);
      throw new UserBadRequestException(PROVIDED_USERNAME_CANNOT_BE_USED);
    }

    Optional<UserEntity> userEntity = userRepository.findByUsername(username);
    if (userEntity.isPresent()) {
      log.error(THE_PROVIDED_USERNAME_WAS_NOT_UNIQUE, username);
      throw new UserBadRequestException(PROVIDED_USERNAME_CANNOT_BE_USED);
    }
  }
}
