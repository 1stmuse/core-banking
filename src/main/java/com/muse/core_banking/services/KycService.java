package com.muse.core_banking.services;

import com.muse.core_banking.dto.kyc.UpdateKycRequestDto;
import com.muse.core_banking.entities.Kyc;
import com.muse.core_banking.repositories.CustomerRepository;
import com.muse.core_banking.repositories.KycRepository;
import com.muse.core_banking.utils.Helpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KycService {

    private final KycRepository kycRepository;
    private final CustomerRepository customerRepository;

    public void updateKyc(UpdateKycRequestDto request, Long customerId) throws BadRequestException {
        var customer = customerRepository.findById(customerId);
        if(!customer.isPresent()){
            throw new BadRequestException("Customer is not available");
        }
        var customerDetail = customer.get();

        Kyc kyc = (kycRepository.findById(customerDetail.getKyc().getId()).isPresent()) ?
                customerDetail.getKyc() : Kyc.builder().build();

        var validKycObj =  Helpers.copyNonNullProperties(request, kyc);
        validKycObj.setCustomer(customerDetail);

        var savedKyc = kycRepository.save(validKycObj);

        customerDetail.setKyc(savedKyc);
        customerRepository.save(customerDetail);
    }

    public Kyc getKyc(Long customerId) throws BadRequestException {
        return kycRepository.findByCustomer_Id(customerId).orElseThrow(
                () -> new BadRequestException("Error getting KYC details")
        );

    }
}
