package com.muse.core_banking.services.auth;

import com.muse.core_banking.dto.auth.EmailPasswordRequest;
import com.muse.core_banking.dto.auth.LoginResponseDto;
import com.muse.core_banking.dto.auth.CreateUserResponseDto;
import com.muse.core_banking.dto.auth.VerifyEmailRequest;
import com.muse.core_banking.entities.users.User;
import com.muse.core_banking.mappers.AuthMapper;
import com.muse.core_banking.repositories.auth.AuthRepository;
import com.muse.core_banking.security.JwtService;
import com.muse.core_banking.services.EmailService;
import com.muse.core_banking.utils.Helpers;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final EmailService emailService;

    public void verifyEmail(VerifyEmailRequest request) throws BadRequestException {
        Optional<User> user = authRepository.findByEmail(request.email());
        if(user.isPresent()){
            var oldUser = user.get();
            oldUser.setEnabled(true);
            authRepository.save(oldUser);
            return;
        }
        throw new BadRequestException("");
    }

    public CreateUserResponseDto saveUser(EmailPasswordRequest request) throws BadRequestException, MessagingException {
        Optional<User> user = authRepository.findByEmail(request.email());
        if(user.isPresent()){
            throw new BadRequestException("User already exist");
        }

        User newUser = User.builder()
                .email(request.email())
                .userType(request.userType())
                .password(passwordEncoder.encode(request.password()))
                .build();



        try {
            emailService.sendVerifyEmailMail(request.email(), Helpers.generateOtp(6), "verify_account", "Account Verification");
            var saveduser = authRepository.save(newUser);

            return authMapper.mapToCreateUserResponse(saveduser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public LoginResponseDto loginWithEmail(EmailPasswordRequest request) throws BadCredentialsException {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        var user = (User) auth.getPrincipal();

        var token = jwtService.generateToken(user);

        return authMapper.mapToLoginResponseDto(user, token);
    }

}
