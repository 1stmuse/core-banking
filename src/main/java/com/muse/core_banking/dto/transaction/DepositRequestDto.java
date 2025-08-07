package com.muse.core_banking.dto.transaction;

public record DepositRequestDto(
        Long amount,
        Long fromAccount
) {
}
