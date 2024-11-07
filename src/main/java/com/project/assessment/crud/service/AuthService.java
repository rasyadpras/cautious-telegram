package com.project.assessment.crud.service;

import com.project.assessment.crud.entity.Account;
import com.project.assessment.crud.entity.Role;
import com.project.assessment.crud.mapper.LoginMapper;
import com.project.assessment.crud.mapper.RegisterMapper;
import com.project.assessment.crud.model.request.AuthRequest;
import com.project.assessment.crud.model.response.LoginResponse;
import com.project.assessment.crud.model.response.RegisterResponse;
import com.project.assessment.crud.repository.UserAccountRepository;
import com.project.assessment.crud.utils.config.ValidationUtil;
import com.project.assessment.crud.utils.constant.RoleUser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAccountRepository accountRepo;
    private final ValidationUtil validation;
    private final RegisterMapper registerMapper;
    private final LoginMapper loginMapper;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${assessment.admin.email}")
    private String adminEmail;
    @Value("${assessment.admin.password}")
    private String adminPassword;

    @PostConstruct
    public void initAdmin() {
        Optional<Account> currentAdmin = accountRepo.findByEmail(adminEmail);
        if (currentAdmin.isPresent()) return;

        Role admin = roleService.getOrSave(RoleUser.ROLE_ADMINISTRATOR);
        Role customer = roleService.getOrSave(RoleUser.ROLE_CUSTOMER);

        Account account = Account.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .roles(List.of(admin, customer))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        accountRepo.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(AuthRequest request) {
        validation.validate(request);
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(roleService.getOrSave(RoleUser.ROLE_CUSTOMER)))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        accountRepo.saveAndFlush(account);
        return registerMapper.toRegisterResponse(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(AuthRequest request) {
        validation.validate(request);
        Account account = accountRepo.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("E-mail not found")
        );

        if (!account.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not active");
        }

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            );
            Authentication authenticated = authenticationManager.authenticate(authentication);
            Account authenticatedUser = (Account) authenticated.getPrincipal();
            String token = jwtService.generateToken(authenticatedUser);

            accountRepo.saveAndFlush(account);
            return loginMapper.toLoginResponse(authenticatedUser, token);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
