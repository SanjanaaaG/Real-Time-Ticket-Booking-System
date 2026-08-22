package com.eventhub.auth_service.Controller;

import com.eventhub.auth_service.DTO.LoginRequestDto;
import com.eventhub.auth_service.DTO.LoginResponseDo;
import com.eventhub.auth_service.DTO.SignUpRequestDTO;
import com.eventhub.auth_service.DTO.SignUpResponseDTo;
import com.eventhub.auth_service.Entity.UserEntity;
import com.eventhub.auth_service.ServiceImplementation.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {

    private final UserServiceImpl userService;

    public User(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<SignUpResponseDTo> signUp(@RequestBody SignUpRequestDTO signUpDTO) {
        SignUpResponseDTo userResponseDTO = userService.signUp(signUpDTO);
        try {
            if (userResponseDTO != null) {
                return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDo> login(@RequestBody LoginRequestDto loginDto){
        LoginResponseDo userEntity = userService.login(loginDto);
        try{
            if((userEntity != null)){
                return new ResponseEntity<>(userEntity, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
