package com.eventhub.auth_service.Mapper;

import com.eventhub.auth_service.DTO.LoginResponseDo;
import com.eventhub.auth_service.DTO.SignUpResponseDTo;
import com.eventhub.auth_service.Entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public SignUpResponseDTo toUserResponse(UserEntity userEntity) {
        SignUpResponseDTo user = new SignUpResponseDTo();
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setRole(userEntity.getRole());
        user.createdAt = LocalDateTime.now();
        return user;
    }

    public LoginResponseDo toLoginResponse(UserEntity userEntity, String token) {
        LoginResponseDo user = new LoginResponseDo();
        user.setUser(userEntity);
        user.setToken(token); // Token will be set later
        return user;
    }
}
