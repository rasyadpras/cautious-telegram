package com.project.assessment.crud.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "E-mail is required")
    @Email(message = "E-mail invalid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
