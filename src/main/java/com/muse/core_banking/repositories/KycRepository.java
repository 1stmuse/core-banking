package com.muse.core_banking.repositories;

import com.muse.core_banking.entities.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface KycRepository  extends JpaRepository<Kyc, Long> {

//    @Query("SELECT k FROM Kyc k WHERE k.customer.id = :customerId")
    Optional<Kyc> findByCustomer_Id( Long customerId);
}
