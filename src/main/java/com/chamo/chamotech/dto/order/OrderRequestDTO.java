package com.chamo.chamotech.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotEmpty(message = "the order must have at least one item")
    private List<@Valid OrderItemDTO> items;
}
