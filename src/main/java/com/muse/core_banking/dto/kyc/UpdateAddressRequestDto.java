package com.muse.core_banking.dto.kyc;

public record UpdateAddressRequestDto(
        String street,
        String city,
        String state
) {
}
