package com.muse.core_banking.repositories;

import com.muse.core_banking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String username);
}
