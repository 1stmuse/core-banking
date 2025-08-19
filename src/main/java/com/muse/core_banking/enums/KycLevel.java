package com.muse.core_banking.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum KycLevel {
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3);

    private final int level;
}
