package com.muse.core_banking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountType {
    SAVINGS(8),
    CURRENT(10),
    FIXED_DEPOSIT(15);


    private final int interestRate;
}
