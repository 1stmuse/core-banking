package com.muse.core_banking.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.muse.core_banking.enums.AccountStatus;
import com.muse.core_banking.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountNo;
    private int interestRate;
    private Long balance;
    private AccountType accountType;

    private AccountStatus accountStatus = AccountStatus.INACTIVE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonBackReference
    private Customer customer;

    @Column(name = "customer_id", updatable = false, insertable = false)
    private Long customerId;

    @CreatedDate
    private Date createdAt;
}
