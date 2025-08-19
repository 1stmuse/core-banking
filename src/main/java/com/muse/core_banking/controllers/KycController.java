package com.muse.core_banking.controllers;

import com.muse.core_banking.dto.kyc.UpdateKycRequestDto;
import com.muse.core_banking.entities.Kyc;
import com.muse.core_banking.handlers.ResponseHandler;
import com.muse.core_banking.services.KycService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.muse.core_banking.enums.ApiMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
@Tag(name = "KYC", description = "KYC endpoinds")
public class KycController {

    private final KycService kycService;

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateKyc(
            @RequestBody UpdateKycRequestDto request,
            @PathVariable String id
            ) throws BadRequestException {
        kycService.updateKyc(request, Long.parseLong(id));
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKyc(
            @PathVariable String id
    ) throws BadRequestException {
        var kyc = kycService.getKyc(Long.parseLong(id));
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .statusCode(OK.value())
                        .data(Optional.of(kyc))
                        .build()
        );
    }
}
