package com.muse.core_banking.repositories;

import com.muse.core_banking.entities.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {

    List<SecurityQuestion> findAllByCustomerId(Long profileId);
}
