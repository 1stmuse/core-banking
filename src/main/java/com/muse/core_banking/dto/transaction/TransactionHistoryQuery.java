package com.muse.core_banking.dto.transaction;

public record TransactionHistoryQuery(
        int page,
        int perPage,
        boolean last3months
) {

    public TransactionHistoryQuery() {
        this(1, 10, false);
    }
}
