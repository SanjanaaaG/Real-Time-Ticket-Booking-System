package com.eventhub.auth_service.DTO;

import com.eventhub.auth_service.Entity.UserEntity;
import lombok.Data;

@Data
public class LoginResponseDo {
    private String token;
    private UserEntity user;
}
