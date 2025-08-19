package com.muse.core_banking.dto.auth;

public record VerifyEmailRequest(String email, int otp) {

}
