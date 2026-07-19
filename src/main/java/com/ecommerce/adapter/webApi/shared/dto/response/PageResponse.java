package com.ecommerce.adapter.webApi.shared.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(List<T> content, PageInfo pageInfo) {

    public record PageInfo(int number, int size, long totalElements, int totalPage) {
    }

    public static <T> PageResponse<T> from(Page<T> p) {
        return new PageResponse<>(p.getContent(),
                new PageInfo(p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages()));
    }
}
