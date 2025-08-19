package com.muse.core_banking.dto.auth;


import com.muse.core_banking.enums.UserType;

public record EmailPasswordRequest(String email, String password, UserType userType) {
}
