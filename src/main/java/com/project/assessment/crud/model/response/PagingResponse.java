package com.project.assessment.crud.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
    private int totalPages;
    private int page;
    private int size;
    private boolean hasNext;
    private boolean hasPrev;
}
