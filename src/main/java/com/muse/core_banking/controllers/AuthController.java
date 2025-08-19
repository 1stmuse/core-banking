package com.muse.core_banking.controllers;

import com.muse.core_banking.dto.auth.*;
import com.muse.core_banking.handlers.ResponseHandler;
import com.muse.core_banking.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import static com.muse.core_banking.enums.ApiMessage.SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Handles authentication for the application")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/email/resetPassword")
    public ResponseEntity<ResponseHandler<?>> resetPassword(
            @RequestBody ResetPasswordRequestDto request
            ) throws BadRequestException {
        authService.resetPassword(request);
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message("Password reset successful")
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/emailSignup")
    public ResponseEntity<ResponseHandler<?>> registerWithEmail(
            @RequestBody EmailPasswordRequest request
    ) throws BadRequestException {
        var user = authService.createUser(request);
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .data(Optional.of(user))
                        .message("An OTP has been sent to your email for verification")
                        .statusCode(OK.value())
                        .build()
        ) ;
    }

    @PostMapping("/email/otp")
    public ResponseEntity<ResponseHandler<?>> requestOtp(
            @RequestBody RequestOtpDto request
            ) throws BadRequestException{
        authService.requestOtp(request);

        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message("Otp has been sent to yur email")
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ResponseHandler<?>> verifyEmail(
            @RequestBody VerifyEmailRequest request
            ) throws BadRequestException {
        authService.verifyEmail(request);

        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/emailLogin")
    public ResponseEntity<ResponseHandler<?>> loginWithEmail(
            @RequestBody EmailPasswordRequest request
    ){
        var response = authService.loginWithEmail(request);

        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .data(Optional.of(response))
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(
            Authentication authUser
    ){
        authService.deleteUser(authUser);

        return ResponseEntity.ok("Success");
    }
}
