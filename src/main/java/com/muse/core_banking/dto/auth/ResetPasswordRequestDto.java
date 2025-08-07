package com.muse.core_banking.dto.auth;

public record ResetPasswordRequestDto(String email, String newPassword) {
}
