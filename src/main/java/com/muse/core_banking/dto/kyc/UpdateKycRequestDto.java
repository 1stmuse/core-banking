package com.muse.core_banking.dto.kyc;

import com.muse.core_banking.enums.IDType;

import java.util.Date;

public record UpdateKycRequestDto(
        IDType idType,
        String email,
        String lastName,
        String firstName,
        Date dateOfBirth,
        String gender,
        String maritalStatus,
        String nationality,
        String phoneNumber,
        String street,
        String city,
        String state,
        String bvn,
        String nin,
        String passportNumber,
        String driverLicenseNumber,
        String voterIdNumber,
        String idImageUrl,
        String occupation,
        String employerName,
        String annualIncomeRange,
        String sourceOfFunds
) {
}
