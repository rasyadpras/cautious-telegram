package com.project.assessment.crud.controller;

import com.project.assessment.crud.mapper.PagingMapper;
import com.project.assessment.crud.model.request.UserRequest;
import com.project.assessment.crud.model.response.PagingResponse;
import com.project.assessment.crud.model.response.SuccessResponse;
import com.project.assessment.crud.model.response.UserResponse;
import com.project.assessment.crud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;
    private final PagingMapper pagingMapper;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SuccessResponse<UserResponse>> addUser(@RequestBody UserRequest request) {
        UserResponse user = userService.create(request);
        SuccessResponse<UserResponse> response = SuccessResponse.<UserResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(user)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SuccessResponse<UserResponse>> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest request
    ) {
        UserResponse user = userService.update(id, request);
        SuccessResponse<UserResponse> response = SuccessResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(user)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SuccessResponse<UserResponse>> getUserById(@PathVariable String id) {
        UserResponse user = userService.getById(id);
        SuccessResponse<UserResponse> response = SuccessResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(user)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<UserResponse>>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name
    ) {
        Page<UserResponse> users = userService.getAll(PageRequest.of(page, size), name);

        PagingResponse paging = pagingMapper.toPaging(
                users.getTotalPages(),
                page,
                size,
                users.hasNext(),
                users.hasPrevious()
        );

        SuccessResponse<List<UserResponse>> response = SuccessResponse.<List<UserResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(users.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SuccessResponse<String>> deleteUser(@PathVariable String id) {
        userService.delete(id);
        SuccessResponse<String> response = SuccessResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("User with id " + id + " has been deleted")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
