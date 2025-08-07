package com.muse.core_banking.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CreateCustomerRequestDto(
        @NotBlank( message = "firstname is required")
        String firstname,
        @NotBlank( message = "lastname is required")
        String lastname,

        @NotBlank( message = "Email is required")
        String email,
        String phone,

        @NotNull( message = "Date of birth is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        Date dateOfBirth
) {
}
