package com.project.assessment.crud.repository;

import com.project.assessment.crud.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
}
