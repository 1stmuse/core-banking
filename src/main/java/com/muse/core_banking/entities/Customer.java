package com.muse.core_banking.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.muse.core_banking.enums.KYCStatus;
import com.muse.core_banking.enums.KycLevel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Date dateOfBirth;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private KYCStatus kycStatus = KYCStatus.UNAVAILABLE;

    private Long dailyTransactionLimit;
    private Long weeklyTransactionLimit;

    @Enumerated(EnumType.STRING)
    private KycLevel kycLevel;

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Address> address;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Account account;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Kyc kyc;

    @CreatedDate
    private Date createdAt;
}
