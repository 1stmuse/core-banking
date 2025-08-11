package com.muse.core_banking.dto.transaction;

import com.muse.core_banking.enums.TransactionStatus;
import com.muse.core_banking.enums.TransactionType;

import java.util.Date;

public record TransactionDetailDto(
        Long id,
        Long amount,
        Long sourceAccount,
        Long destinationAccount,
        TransactionType type,
        TransactionStatus status,
        String transactionRef,
        Date date
) {
}
