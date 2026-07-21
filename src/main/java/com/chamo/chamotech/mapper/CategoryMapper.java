package com.chamo.chamotech.mapper;

import com.chamo.chamotech.dto.category.CategoryResponseDTO;
import com.chamo.chamotech.entity.CategoryEntity;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryResponseDTO toResponseDTO(CategoryEntity category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
