package com.ecommerce.adapter.webApi.category;

import com.ecommerce.adapter.webApi.category.dto.request.CategoryCreateRequest;
import com.ecommerce.adapter.webApi.category.dto.response.CategoryResponse;
import com.ecommerce.adapter.webApi.category.mapper.CategoryMapper;
import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.application.category.provided.CategoryModify;
import com.ecommerce.domain.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequestMapping("/api/admin/category")
@RestController
@RequiredArgsConstructor
public class CategoryAdminApi {

    private final CategoryModify categoryModify;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@RequestBody CategoryCreateRequest request) {
        Category category = categoryModify.create(request);
        CategoryResponse response = categoryMapper.toResponse(category);
        URI location = URI.create("/api/category/" + response.id());
        return ResponseEntity.created(location).body(ApiResponse.ok(response));
    }
}
