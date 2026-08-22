package com.eventhub.auth_service.Repository;

import com.eventhub.auth_service.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long>{
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
