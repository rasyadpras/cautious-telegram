package com.project.assessment.crud.service;

import com.project.assessment.crud.entity.Account;
import com.project.assessment.crud.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserAccountService implements UserDetailsService {
    private final UserAccountRepository accountRepo;

    @Transactional(readOnly = true)
    public Account findId(String id) {
        return accountRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepo.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("E-mail not found")
        );
    }
}
