package com.eventhub.auth_service.DTO;

import lombok.Data;

@Data
public class LoginRequestDto {
    public String username;
    public String password;
}
