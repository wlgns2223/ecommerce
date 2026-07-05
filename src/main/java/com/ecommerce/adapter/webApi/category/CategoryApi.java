package com.ecommerce.adapter.webApi.category;

import com.ecommerce.adapter.webApi.category.dto.response.CategoryResponse;
import com.ecommerce.adapter.webApi.category.mapper.CategoryMapper;
import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.application.category.provided.CategoryFinder;
import com.ecommerce.domain.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryApi {

    private final CategoryFinder categoryFinder;
    private final CategoryMapper categoryMapper;


    @GetMapping("/{id}")
    ApiResponse<CategoryResponse> findById(@PathVariable Long id) {
        Category category = categoryFinder.find(id);
        return ApiResponse.ok(categoryMapper.toResponse(category));

    }

}
