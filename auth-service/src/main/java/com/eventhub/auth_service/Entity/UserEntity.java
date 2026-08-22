package com.eventhub.auth_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue
    public Long userId;
    public String username;
    public String email;
    public String password;
    public UserRoleEnum role;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
