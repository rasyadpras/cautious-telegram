package com.project.assessment.crud.repository;

import com.project.assessment.crud.entity.Role;
import com.project.assessment.crud.utils.constant.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(RoleUser role);
}
