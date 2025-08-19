package com.muse.core_banking.controllers;

import com.muse.core_banking.dto.securityQuestion.AddSecurityQuestionDto;
import com.muse.core_banking.dto.securityQuestion.UpdateSecurityQuestionDto;
import com.muse.core_banking.handlers.ResponseHandler;
import com.muse.core_banking.services.SecurityQuestionService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.muse.core_banking.enums.ApiMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("/securityQuestion")
public class SecurityQuestionController {

    private final SecurityQuestionService securityQuestionService;

    @PostMapping()
    public ResponseEntity<?> addSecurityQuestion(
            @RequestBody List<AddSecurityQuestionDto> request
            ) throws BadRequestException {

        securityQuestionService.addSecurityQuestion(request);
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .statusCode(OK.value())
                        .message(SUCCESS.getMessage())
                        .build()
        );
    }

    @PatchMapping()
    public ResponseEntity<?> updateSecurityQuestion(
            @RequestBody List<UpdateSecurityQuestionDto> request
    ) throws BadRequestException {

        securityQuestionService.updateSecurityQuestion(request);
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .statusCode(OK.value())
                        .message(SUCCESS.getMessage())
                        .build()
        );
    }
}
