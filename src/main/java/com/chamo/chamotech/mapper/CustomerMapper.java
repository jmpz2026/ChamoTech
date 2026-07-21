package com.chamo.chamotech.mapper;

import com.chamo.chamotech.dto.customer.CustomerResponseDTO;
import com.chamo.chamotech.entity.CustomerEntity;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponseDTO toResponseDTO(CustomerEntity customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        return dto;
    }
}
