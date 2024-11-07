package com.project.assessment.crud.mapper;

import com.project.assessment.crud.model.response.PagingResponse;
import org.springframework.stereotype.Component;

@Component
public class PagingMapper {
    public PagingResponse toPaging(int totalPage, int page, int size, boolean hasNext, boolean hasPrev) {
        return PagingResponse.builder()
                .totalPages(totalPage)
                .page(page)
                .size(size)
                .hasNext(hasNext)
                .hasPrev(hasPrev)
                .build();
    }
}
