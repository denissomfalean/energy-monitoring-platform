package com.denissomfalean.usermanagementservice.core.persistence;

import com.denissomfalean.usermanagementservice.core.persistence.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByUsernameAndIdNot(String username, Long id);
}
