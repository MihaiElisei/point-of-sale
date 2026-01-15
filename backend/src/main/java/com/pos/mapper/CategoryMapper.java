package com.pos.mapper;

import com.pos.models.Category;
import com.pos.payload.dto.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {

    return CategoryDto.builder()
        .id(category.getId())
        .name(category.getName())
        .storeId(category.getStore()!=null?category.getStore().getId():null)
        .build();
    }
}
