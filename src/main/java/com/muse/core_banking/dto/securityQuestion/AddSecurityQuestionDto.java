package com.muse.core_banking.dto.securityQuestion;

public record AddSecurityQuestionDto(
        String question,
        String answer,
        String customerId
) {
}
