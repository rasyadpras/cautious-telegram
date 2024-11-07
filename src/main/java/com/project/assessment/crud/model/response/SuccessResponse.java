package com.project.assessment.crud.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private PagingResponse paging;
}
