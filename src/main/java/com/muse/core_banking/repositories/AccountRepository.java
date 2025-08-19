package com.muse.core_banking.repositories;

import com.muse.core_banking.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCustomer_Id(Long customerId);

    Optional<Account> findByAccountNo(Long accountNo);
}
