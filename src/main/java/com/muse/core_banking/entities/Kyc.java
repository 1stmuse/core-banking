package com.muse.core_banking.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.muse.core_banking.enums.IDType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Info
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String nationality;

    // Contact Info
    private String email;
    private String phoneNumber;
    private String street;
    private String city;
    private String state;

    // Identification
    private String bvn;
    private String nin;
    private String passportNumber;
    private String driversLicenseNumber;
    private String voterIdNumber;
    private IDType idType;
    private String idImageUrl;

    // Compliance
    private String kycLevel;
    private Boolean bvnVerified;
    private Boolean ninVerified;
    private Boolean addressVerified;
    private String amlCheckStatus;

    // Employment & Financial
    private String occupation;
    private String employerName;
    private String annualIncomeRange;
    private String sourceOfFunds;

    // Audit
    @CreatedBy
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonBackReference
    private Customer customer;

    @Column(name = "customer_id", updatable = false, insertable = false)
    private Long customerId;
}

