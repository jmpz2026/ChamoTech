package com.chamo.chamotech.mapper;

import com.chamo.chamotech.dto.order.OrderLineResponseDTO;
import com.chamo.chamotech.dto.order.OrderResponseDTO;
import com.chamo.chamotech.entity.OrderEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponseDTO toResponseDTO(OrderEntity order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setStatus(order.getStatus().name());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setTotal(order.getTotal());

        List<OrderLineResponseDTO> lines = order.getLines().stream()
                .map(line -> {
                    OrderLineResponseDTO lineDTO = new OrderLineResponseDTO();
                    lineDTO.setProductId(line.getProduct().getId());
                    lineDTO.setProductName(line.getProduct().getName());
                    lineDTO.setQuantity(line.getQuantity());
                    lineDTO.setUnitPrice(line.getUnitPrice());
                    lineDTO.setSubtotal(line.getSubtotal());
                    return lineDTO;
                })
                .collect(Collectors.toList());
        dto.setLines(lines);

        return dto;
    }
}
