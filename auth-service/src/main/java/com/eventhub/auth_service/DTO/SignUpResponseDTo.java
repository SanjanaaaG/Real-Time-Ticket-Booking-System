package com.eventhub.auth_service.DTO;

import com.eventhub.auth_service.Entity.UserRoleEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignUpResponseDTo {
    public String username;
    public String email;
    public String password;
    public UserRoleEnum role;
    public LocalDateTime createdAt;
}
