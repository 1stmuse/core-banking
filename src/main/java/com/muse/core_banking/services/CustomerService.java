package com.muse.core_banking.services;

import com.muse.core_banking.dto.customer.CreateCustomerRequestDto;
import com.muse.core_banking.entities.Account;
import com.muse.core_banking.entities.Customer;
import com.muse.core_banking.entities.Kyc;
import com.muse.core_banking.entities.User;
import com.muse.core_banking.enums.AccountStatus;
import com.muse.core_banking.enums.AccountType;
import com.muse.core_banking.enums.KYCStatus;
import com.muse.core_banking.repositories.AccountRepository;
import com.muse.core_banking.repositories.CustomerRepository;
import com.muse.core_banking.repositories.KycRepository;
import com.muse.core_banking.utils.Helpers;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.muse.core_banking.enums.AccountStatus.*;
import static com.muse.core_banking.enums.AccountType.SAVINGS;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final KycRepository kycRepository;

    public Customer getProfile(Long id) throws BadRequestException {
        return findById(id).orElseThrow(() -> new BadRequestException("Customer not found"));
    }

    public Customer updateCustomerInfo(
            CreateCustomerRequestDto request , Long customerId
    ) throws BadRequestException {
        var customer = findById(customerId);

        if(customer.isPresent()){
            var updatedCustomer = customer.get();
            Helpers.copyNonNullProperties(request, updatedCustomer);

            return customerRepository.save(updatedCustomer);
        }
        else {
            throw new BadRequestException("Customer not found");
        }
    }

    public Customer createCustomer(CreateCustomerRequestDto request, Authentication authUser) throws BadRequestException {
        User user = Helpers.getConnectedUser(authUser);
        var customer = findCustomerByEmail(request.email());

        if(customer.isPresent()){
            throw new BadRequestException("Customer already exists");
        }
        var newCustomer = Customer.builder()
                .email(request.email())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .dateOfBirth(request.dateOfBirth())
                .phone(request.phone())
                .userId(user.getId())
                .kycStatus(KYCStatus.UNAVAILABLE)
                .build();
        var savedCustomer = customerRepository.save(newCustomer);
        var kyc = Kyc.builder()
                .firstName(savedCustomer.getFirstname())
                .lastName(savedCustomer.getLastname())
                .email(savedCustomer.getEmail())
                .bvnVerified(false)
                .customer(savedCustomer)
                .customerId(savedCustomer.getId())
                .ninVerified(false)
                .build();

        var account = Account.builder()
                .balance(0L)
                .accountNo(Long.parseLong(Helpers.accountGenerator()))
                .accountStatus(INACTIVE)
                .accountType(SAVINGS)
                .customer(savedCustomer)
                .customerId(savedCustomer.getId())
                .interestRate(SAVINGS.getInterestRate())
                .build();

        accountRepository.save(account);
        kycRepository.save(kyc);

        return  savedCustomer;
    }

    private Optional<Customer> findCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    private Optional<Customer> findById(Long id){
        return customerRepository.findById(id);
    }
}
