package com.muse.core_banking.dto.transaction;

public record PageResult(
        int page,
        int perPage,
        int pageSize
) {
}
