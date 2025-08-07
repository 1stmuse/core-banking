package com.muse.core_banking.mappers;

import com.muse.core_banking.dto.auth.CreateUserResponseDto;
import com.muse.core_banking.dto.auth.LoginResponseDto;
import com.muse.core_banking.entities.User;
import org.springframework.stereotype.Service;

@Service
public class AuthMapper {

    public CreateUserResponseDto mapToCreateUserResponse(User user){

        return CreateUserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userType(user.getUserType().name())
                .enabled(user.isEnabled())
                .build();
    }

    public LoginResponseDto mapToLoginResponseDto(User user, String token){

        return LoginResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userType(user.getUserType().name())
                .enabled(user.isEnabled())
                .token(token)
                .build();
    }
}
