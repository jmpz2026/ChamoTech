package com.chamo.chamotech.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

    private Long id;
    private LocalDateTime createdAt;
    private String status;
    private Long customerId;
    private String customerName;
    private BigDecimal total;
    private List<OrderLineResponseDTO> lines;
}
