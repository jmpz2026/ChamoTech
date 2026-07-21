package com.chamo.chamotech.mapper;

import com.chamo.chamotech.dto.product.ProductResponseDTO;
import com.chamo.chamotech.dto.tag.TagResponseDTO;
import com.chamo.chamotech.entity.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponseDTO toResponseDTO(ProductEntity product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());

        List<TagResponseDTO> tags = product.getTags().stream()
                .map(TagMapper::toResponseDTO)
                .collect(Collectors.toList());
        dto.setTags(tags);

        return dto;
    }
}
