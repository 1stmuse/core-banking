package com.muse.core_banking.controllers;

import com.muse.core_banking.dto.auth.EmailPasswordRequest;
import com.muse.core_banking.dto.auth.VerifyEmailRequest;
import com.muse.core_banking.handlers.ResponseHandler;
import com.muse.core_banking.services.auth.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import static com.muse.core_banking.enums.ApiMessage.SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/emailSignup")
    public ResponseEntity<ResponseHandler> registerWithEmail(
            @RequestBody EmailPasswordRequest request
    ) throws BadRequestException, MessagingException {
        var user = authService.saveUser(request);
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .data(Optional.of(user))
                        .message("An OTP has been sent to your email for verification")
                        .statusCode(OK.value())
                        .build()
        ) ;
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ResponseHandler> verifyEmail(
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
    public ResponseEntity<ResponseHandler> loginWithEmail(
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
}
