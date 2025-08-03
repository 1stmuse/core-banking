package com.muse.core_banking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApiMessage {

    SUCCESS("Success");

    @Getter
    private String message;
}
