package com.project.assessment.crud.controller;

import com.project.assessment.crud.model.request.AuthRequest;
import com.project.assessment.crud.model.response.LoginResponse;
import com.project.assessment.crud.model.response.RegisterResponse;
import com.project.assessment.crud.model.response.SuccessResponse;
import com.project.assessment.crud.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(
            path = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SuccessResponse<RegisterResponse>> register(@RequestBody AuthRequest request) {
        RegisterResponse register = authService.register(request);
        SuccessResponse<RegisterResponse> response = SuccessResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody AuthRequest request) {
        LoginResponse login = authService.login(request);
        SuccessResponse<LoginResponse> response = SuccessResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(login)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
