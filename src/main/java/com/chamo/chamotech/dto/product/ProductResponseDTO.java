package com.chamo.chamotech.dto.product;

import com.chamo.chamotech.dto.tag.TagResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductResponseDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;
    private List<TagResponseDTO> tags;
}
