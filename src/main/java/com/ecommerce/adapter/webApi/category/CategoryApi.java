package com.ecommerce.adapter.webApi.category;

import com.ecommerce.adapter.webApi.category.dto.request.CategoryCreateRequest;
import com.ecommerce.adapter.webApi.category.dto.response.CategoryResponse;
import com.ecommerce.adapter.webApi.category.mapper.CategoryMapper;
import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.application.category.provided.CategoryFinder;
import com.ecommerce.application.category.provided.CategoryModify;
import com.ecommerce.domain.category.entity.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryApi {

    private final CategoryFinder categoryFinder;
    private final CategoryModify categoryModify;
    private final CategoryMapper categoryMapper;


    @GetMapping("/{id}")
    ApiResponse<CategoryResponse> getById(@PathVariable Long id) {
        Category category = categoryFinder.find(id);
        return ApiResponse.ok(categoryMapper.toResponse(category));

    }

    @PostMapping
    ApiResponse<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest request){
        Category category = categoryModify.create(request);
        return ApiResponse.ok(categoryMapper.toResponse(category));
    }

}
