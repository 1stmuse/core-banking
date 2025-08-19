package com.muse.core_banking.services;

import com.muse.core_banking.dto.kyc.UpdateAddressRequestDto;
import com.muse.core_banking.entities.Address;
import com.muse.core_banking.entities.Kyc;
import com.muse.core_banking.repositories.AddressRepository;
import com.muse.core_banking.repositories.CustomerRepository;
import com.muse.core_banking.repositories.KycRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final KycRepository kycRepository;
    private final CustomerRepository customerRepository;

    public void addAddress(UpdateAddressRequestDto request, Long customerId) throws BadRequestException {
        var customer = customerRepository.findById(customerId);
        if(!customer.isPresent()){
            throw new BadRequestException("Customer is not available");
        }
        var customerDetail = customer.get();
        Kyc kyc = (kycRepository.findById(customerDetail.getKyc().getId()).isPresent()) ?
                customerDetail.getKyc() : Kyc.builder().build();

        var address = Address.builder()
                .state(request.state())
                .street(request.street())
                .city(request.city())
                .customer(customerDetail)
                .customerId(customerDetail.getId())
                .build();

        kyc.setStreet(request.street());
        kyc.setCity(request.city());
        kyc.setState(request.state());

        var savedCustomerAddresses = customerDetail.getAddress();
        var savedAddress = addressRepository.save(address);

        //Update saved customer addresses with new address
        // Check if an address with the same id already exists
        Optional<Address> existingAddress = savedCustomerAddresses.stream()
                .filter(adr -> adr.getId().equals(savedAddress.getId()))
                .findFirst();

        if (existingAddress.isPresent()) {
            int index = savedCustomerAddresses.indexOf(existingAddress.get());
            savedCustomerAddresses.set(index, savedAddress);
        } else {
            savedCustomerAddresses.add(savedAddress);
        }

        customerDetail.setAddress(savedCustomerAddresses);
        customerRepository.save(customerDetail);
        kycRepository.save(kyc);
    }
}
