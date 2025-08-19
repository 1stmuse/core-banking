package com.muse.core_banking.dto.transaction;

public record WithdrawalRequestDto(
        Long amount,
        Long destinationAcc
) {
}
