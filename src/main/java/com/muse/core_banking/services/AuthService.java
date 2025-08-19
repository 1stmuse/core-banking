package com.muse.core_banking.services;

import com.muse.core_banking.dto.auth.*;
import com.muse.core_banking.entities.User;
import com.muse.core_banking.mappers.AuthMapper;
import com.muse.core_banking.repositories.AuthRepository;
import com.muse.core_banking.repositories.CustomerRepository;
import com.muse.core_banking.security.JwtService;
import com.muse.core_banking.utils.Helpers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final EmailService emailService;
    private final CustomerRepository customerRepository;

    public void resetPassword(ResetPasswordRequestDto request) throws BadRequestException {
        Optional<User> user = findUser(request.email());
        if(user.isEmpty()){
            var savedUser = user.get();
            savedUser.setPassword(passwordEncoder.encode(request.newPassword()));
            authRepository.save(savedUser);
        }else {
            throw new BadRequestException("Bad request");
        }
    }

    public void requestOtp(RequestOtpDto request) throws BadRequestException{
        Optional<User> user = findUser(request.email());
        if(user.isPresent()){
            try{
                emailService.sendVerifyEmailMail(request.email(), Helpers.generateOtp(6), "verify_account", "OTP request");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new BadRequestException("User not found");
    }

    public void verifyEmail(VerifyEmailRequest request) throws BadRequestException {
        Optional<User> user = findUser(request.email());
        if(user.isPresent()){
            var oldUser = user.get();
            oldUser.setEnabled(true);
            authRepository.save(oldUser);
            return;
        }
        throw new BadRequestException("Could not verify email");
    }

    public CreateUserResponseDto createUser(EmailPasswordRequest request) throws BadRequestException {
        Optional<User> user = findUser(request.email());
        if(user.isPresent()){
            throw new BadRequestException("User already exist");
        }

        User newUser = User.builder()
                .email(request.email())
                .userType(request.userType())
                .password(passwordEncoder.encode(request.password()))
                .build();


        try {
//            emailService.sendVerifyEmailMail(request.email(), Helpers.generateOtp(6), "verify_account", "Account Verification");
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

    public void deleteUser(Authentication authUser){
        var user = Helpers.getConnectedUser(authUser);
        authRepository.deleteById(user.getId());
        customerRepository.deleteByUserId(user.getId());
    }

    private Optional<User> findUser(String email){
        return authRepository.findByEmail(email);
    }

}
