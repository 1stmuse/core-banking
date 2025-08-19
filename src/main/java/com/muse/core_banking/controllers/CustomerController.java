package com.muse.core_banking.controllers;

import com.muse.core_banking.dto.customer.CreateCustomerRequestDto;
import com.muse.core_banking.enums.ApiMessage;
import com.muse.core_banking.handlers.ResponseHandler;
import com.muse.core_banking.services.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import static com.muse.core_banking.enums.ApiMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Handles all Customer endpoints")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/")
    public ResponseEntity<?> createCustomer(
            @Valid @RequestBody CreateCustomerRequestDto requestDto,
            Authentication authUser
            ) throws BadRequestException {
        var createdCustomer = customerService.createCustomer(requestDto, authUser);

        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .data(Optional.of(createdCustomer))
                        .statusCode(OK.value())
                        .message(SUCCESS.getMessage())
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @RequestBody CreateCustomerRequestDto request,
            @PathVariable String id
    ) throws BadRequestException {
           var customer = customerService.updateCustomerInfo(request, Long.parseLong(id));

           return ResponseEntity.ok(
                   ResponseHandler.builder()
                           .data(Optional.of(customer))
                           .statusCode(OK.value())
                           .message(SUCCESS.getMessage())
                           .build()
           );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerProfile(
            @PathVariable String id
    ) throws BadRequestException {
        var customer = customerService.getProfile(Long.parseLong(id));
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .data(Optional.of(customer))
                        .statusCode(OK.value())
                        .build()
        );
    }
}
