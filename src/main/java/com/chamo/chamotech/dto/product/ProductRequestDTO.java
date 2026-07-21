package com.chamo.chamotech.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "stock is required")
    @PositiveOrZero(message = "stock cannot be negative")
    private Integer stock;

    @NotNull(message = "categoryId is required")
    private Long categoryId;

    private List<Long> tagIds = new ArrayList<>();
}
