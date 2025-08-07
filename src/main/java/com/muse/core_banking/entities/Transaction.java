package com.muse.core_banking.entities;

import com.muse.core_banking.enums.TransactionStatus;
import com.muse.core_banking.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private Long sourceAccount;
    private Long destinationAccount;
    private TransactionType type;
    private TransactionStatus status;
    private String transactionRef;

    @CreatedDate
    private Date createdAt;
}
