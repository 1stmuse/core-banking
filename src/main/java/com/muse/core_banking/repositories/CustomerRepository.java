package com.muse.core_banking.repositories;

import com.muse.core_banking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findById(Long id);

    void deleteByUserId(Long userID);
}
