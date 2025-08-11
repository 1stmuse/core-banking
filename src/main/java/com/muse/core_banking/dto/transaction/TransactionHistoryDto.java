package com.muse.core_banking.dto.transaction;

import java.util.List;

public record TransactionHistoryDto(
        List<TransactionDetailDto> transactions,
        PageResult pageData
) {
}
