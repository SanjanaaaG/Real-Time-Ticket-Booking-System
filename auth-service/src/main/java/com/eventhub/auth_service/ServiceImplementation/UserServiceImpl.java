package com.eventhub.auth_service.ServiceImplementation;

import com.eventhub.auth_service.DTO.LoginRequestDto;
import com.eventhub.auth_service.DTO.LoginResponseDo;
import com.eventhub.auth_service.DTO.SignUpRequestDTO;
import com.eventhub.auth_service.DTO.SignUpResponseDTo;
import com.eventhub.auth_service.Entity.UserEntity;
import com.eventhub.auth_service.Entity.UserRoleEnum;
import com.eventhub.auth_service.Exception.UserAlreadyExistsException;
import com.eventhub.auth_service.Mapper.UserMapper;
import com.eventhub.auth_service.Repository.UserRepo;
import com.eventhub.auth_service.Service.UserService;
import com.eventhub.auth_service.Util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;

    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper , JWTUtil jwtUtil) {
        this.userMapper = userMapper;
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserEntity loadUserByUsername(String username) {
        return null;
    }

    @Override
    public SignUpResponseDTo signUp(SignUpRequestDTO signUpDTO) {
        if (userRepo.existsByUsername(signUpDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signUpDTO.getUsername());
        userEntity.setEmail(signUpDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        userEntity.setRole(UserRoleEnum.USER);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());

        userRepo.save(userEntity);
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public LoginResponseDo login(LoginRequestDto loginDto) {
        UserEntity userEntity = userRepo.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token =  jwtUtil.generateToken(userEntity);
        return userMapper.toLoginResponse(userEntity, token);

    }
}
