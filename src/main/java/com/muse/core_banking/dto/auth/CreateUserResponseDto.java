package com.muse.core_banking.dto.auth;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponseDto {

    private String email;
    private boolean enabled;
    private Long id;
    private String userType;
}
