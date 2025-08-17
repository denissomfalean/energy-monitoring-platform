package com.denissomfalean.usermanagementservice.core.service.impl;

import com.denissomfalean.usermanagementservice.api.dto.UserInfoResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserLoginResponseDto;
import com.denissomfalean.usermanagementservice.api.dto.UserSaveRequestDto;
import com.denissomfalean.usermanagementservice.core.handler.exception.UserBadRequestException;
import com.denissomfalean.usermanagementservice.core.mapper.UserMapper;
import com.denissomfalean.usermanagementservice.core.persistence.UserRepository;
import com.denissomfalean.usermanagementservice.core.persistence.entity.AccessRole;
import com.denissomfalean.usermanagementservice.core.persistence.entity.UserEntity;
import com.denissomfalean.usermanagementservice.core.security.AuthenticatedUser;
import com.denissomfalean.usermanagementservice.core.security.JwtProvider;
import com.denissomfalean.usermanagementservice.core.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private static final String NO_USERS_COULD_BE_FOUND = "No users could be found";
  private static final String PROVIDED_USERNAME_CANNOT_BE_USED = "Provided username cannot be used";
  private static final String NO_USER_COULD_BE_FOUND_FOR_ID = "No user could be found for id {{}}";
  private static final String THE_PROVIDED_USERNAME_WAS_NOT_UNIQUE =
      "The provided username was not unique: {{}}";
  private static final String ACCESS_DENIED_MESSAGE =
      "You do not have permission to access this page";
  public static final String AUTHENTICATED_USER_HAS_NO_ROLE_ASSIGNED =
      "Authenticated user has no role assigned";

  private final UserRepository userRepository;
  private final JwtProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserLoginResponseDto login(Authentication authentication) {
    AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

    String jwt = jwtTokenProvider.generateJwtToken(authenticatedUser);

    return UserMapper.toUserLoginResponseDto(
        (AuthenticatedUser) authentication.getPrincipal(), jwt);
  }

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
    checkUserIdAccessRights(id);
    UserEntity userEntity = getUserEntityById(id);

    return UserMapper.toUserInfoResponseDto(userEntity);
  }

  @Override
  public UserInfoResponseDto save(UserSaveRequestDto userSaveRequestDto) {
    checkIfUsernameUnique(userSaveRequestDto.getUsername());

    UserEntity userEntity = UserMapper.toUserEntity(userSaveRequestDto);
    userEntity.setPassword(passwordEncoder.encode(userSaveRequestDto.getPassword()));

    return UserMapper.toUserInfoResponseDto(userRepository.save(userEntity));
  }

  @Override
  public UserInfoResponseDto updateUserById(Long id, UserSaveRequestDto userSaveRequestDto) {
    checkUserIdAccessRights(id);
    checkIfUsernameUnique(userSaveRequestDto.getUsername(), id);
    UserEntity existingUserEntity = getUserEntityById(id);

    UserEntity userEntityToUpdate = UserMapper.toUserEntity(userSaveRequestDto);
    userEntityToUpdate.setId(existingUserEntity.getId());
    userEntityToUpdate.setPassword(passwordEncoder.encode(userSaveRequestDto.getPassword()));

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
      throw new UserBadRequestException(NO_USERS_COULD_BE_FOUND);
    }

    return optionalUserEntity.get();
  }

  private void checkIfUsernameUnique(String username) {
    Optional<UserEntity> userEntity = userRepository.findByUsername(username);

    if (userEntity.isPresent()) {
      log.error(THE_PROVIDED_USERNAME_WAS_NOT_UNIQUE, username);
      throw new UserBadRequestException(PROVIDED_USERNAME_CANNOT_BE_USED);
    }
  }

  private void checkIfUsernameUnique(String username, Long existingId) {
    Optional<UserEntity> userEntity = userRepository.findByUsernameAndIdNot(username, existingId);

    if (userEntity.isPresent()) {
      log.error(THE_PROVIDED_USERNAME_WAS_NOT_UNIQUE, username);
      throw new UserBadRequestException(PROVIDED_USERNAME_CANNOT_BE_USED);
    }
  }

  private void checkUserIdAccessRights(Long id) {
    Long loggedUserId = getCurrentUserId();
    AccessRole accessRole = getCurrentUserRole();

    if (AccessRole.CLIENT.equals(accessRole) && !(loggedUserId.equals(id))) {
      throw new UserBadRequestException(ACCESS_DENIED_MESSAGE);
    }
  }

  private AccessRole getCurrentUserRole() {
    String accessRoleAsString =
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElseThrow(
                () -> new UserBadRequestException(AUTHENTICATED_USER_HAS_NO_ROLE_ASSIGNED));

    return AccessRole.fromString(accessRoleAsString);
  }

  private Long getCurrentUserId() {
    String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return Long.parseLong(userId);
  }
}
