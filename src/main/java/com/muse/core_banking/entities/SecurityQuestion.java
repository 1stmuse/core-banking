package com.muse.core_banking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@Data
public class SecurityQuestion {

    @Id
    private Long id;
    private String Question;
    private String Answer;
    private Long customerId;
}
