package com.muse.core_banking.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponseDto {

    private String email;
    private boolean enabled;
    private Long id;
    private String userType;
    private String token;
}
