package com.muse.core_banking.dto.securityQuestion;

public record UpdateSecurityQuestionDto(
        Long id,
        String answer,
        String customerId
) {
}
