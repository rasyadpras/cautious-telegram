package com.project.assessment.crud.mapper;

import com.project.assessment.crud.entity.Account;
import com.project.assessment.crud.model.response.RegisterResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {
    public RegisterResponse toRegisterResponse(Account account) {
        return RegisterResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
