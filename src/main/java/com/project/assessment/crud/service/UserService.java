package com.project.assessment.crud.service;

import com.project.assessment.crud.entity.User;
import com.project.assessment.crud.mapper.UserMapper;
import com.project.assessment.crud.model.request.UserRequest;
import com.project.assessment.crud.model.response.UserResponse;
import com.project.assessment.crud.repository.UserRepository;
import com.project.assessment.crud.utils.config.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final UserMapper mapper;
    private final ValidationUtil validation;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse create(UserRequest request) {
        validation.validate(request);
        User user = User.builder()
                .name(request.getName())
                .age(request.getAge())
                .membership(request.isMembership())
                .createdAt(LocalDateTime.now())
                .build();
        userRepo.saveAndFlush(user);
        return mapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(String id) {
        User user = findId(id);
        return mapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #name")
    public Page<UserResponse> getAll(Pageable pageable, String name) {
        Page<User> users;
        if (name != null) {
            users = userRepo.findAllByNameContainingIgnoreCase(name, pageable);
        } else {
            users = userRepo.findAll(pageable);
        }
        return users.map(mapper::toUserResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponse update(String id, UserRequest request) {
        validation.validate(request);
        User user = findId(id);
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setMembership(request.isMembership());
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.saveAndFlush(user);
        return mapper.toUserResponse(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        User user = findId(id);
        userRepo.delete(user);
    }

    @Transactional(readOnly = true)
    public User findId(String id) {
        return userRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
    }
}
