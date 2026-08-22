package com.eventhub.auth_service.DTO;

import lombok.Data;

@Data
public class SignUpRequestDTO {
    public String username;
    public String email;
    public String password;
}
