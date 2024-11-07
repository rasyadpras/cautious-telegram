package com.project.assessment.crud.service;

import com.project.assessment.crud.entity.Role;
import com.project.assessment.crud.repository.RoleRepository;
import com.project.assessment.crud.utils.constant.RoleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepo;

    @Transactional(rollbackFor = Exception.class)
    public Role getOrSave(RoleUser role) {
        return roleRepo.findByRole(role).orElseGet(
                () -> roleRepo.saveAndFlush(Role.builder().role(role).build())
        );
    }
}
