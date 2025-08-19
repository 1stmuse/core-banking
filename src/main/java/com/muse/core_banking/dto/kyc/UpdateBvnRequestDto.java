package com.muse.core_banking.dto.kyc;

import java.util.Date;

public record UpdateBvnRequestDto(String phone, String bvn, Date dob) {
}
