package com.ecommerce.adapter.webApi.category.mapper;

import com.ecommerce.adapter.webApi.category.dto.response.CategoryResponse;
import com.ecommerce.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "slug", source = "slug.value")
    @Mapping(target = "level", source = "level.value")
    CategoryResponse toResponse(Category category);
}
