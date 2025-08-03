package com.muse.core_banking.repositories.auth;

import com.muse.core_banking.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String username);
}
