package com.eventhub.auth_service.Service;

import com.eventhub.auth_service.DTO.LoginRequestDto;
import com.eventhub.auth_service.DTO.LoginResponseDo;
import com.eventhub.auth_service.DTO.SignUpRequestDTO;
import com.eventhub.auth_service.DTO.SignUpResponseDTo;
import com.eventhub.auth_service.Entity.UserEntity;

public interface UserService {
    public UserEntity loadUserByUsername(String username);
    public SignUpResponseDTo signUp(SignUpRequestDTO signUpDTO);
    public LoginResponseDo login(LoginRequestDto loginDto);

}
