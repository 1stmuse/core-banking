package com.muse.core_banking.repositories;

import com.muse.core_banking.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
