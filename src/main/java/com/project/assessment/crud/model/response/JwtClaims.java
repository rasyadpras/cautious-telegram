package com.project.assessment.crud.model.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtClaims {
    private String userId;
    private List<String> roles;
}
