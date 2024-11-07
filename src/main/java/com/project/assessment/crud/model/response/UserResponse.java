package com.project.assessment.crud.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse implements Serializable {
    private String id;
    private String name;
    private int age;
    private boolean membership;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
