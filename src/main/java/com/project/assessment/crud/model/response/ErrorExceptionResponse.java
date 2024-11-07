package com.project.assessment.crud.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorExceptionResponse {
    private int statusCode;
    private String message;
    private String error;
}
