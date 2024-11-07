package com.project.assessment.crud.mapper;

import com.project.assessment.crud.entity.Account;
import com.project.assessment.crud.model.response.LoginResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {
    public LoginResponse toLoginResponse(Account account, String token) {
        return LoginResponse.builder()
                .email(account.getEmail())
                .token(token)
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}
