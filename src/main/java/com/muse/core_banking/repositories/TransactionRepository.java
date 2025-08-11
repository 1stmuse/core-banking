package com.muse.core_banking.repositories;

import com.muse.core_banking.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.sourceAccount = :accountNo
       OR t.destinationAccount = :accountNo
    """)
    Page<Transaction> findByAccount(@Param("accountNo") Long accountNo, Pageable pageable);

    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.createdAt >= CURRENT_DATE
      AND t.createdAt < FUNCTION('TIMESTAMPADD', DAY, 1, CURRENT_DATE)
      AND t.sourceAccount = :accountNo
      OR t.destinationAccount = :accountNo
""")
    List<Transaction> findTodayTransactions(@Param("accountNo") Long accountNo);

    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.createdAt >= :startDate
      AND t.createdAt < :endDate
      AND t.sourceAccount = :accountNo
      OR t.destinationAccount = :accountNo
""")
    List<Transaction> findTransactionsWithinPeriod(@Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate,
                                                   @Param("accountNo") Long accountNo);
}
