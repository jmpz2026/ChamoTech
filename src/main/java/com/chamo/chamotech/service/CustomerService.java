package com.chamo.chamotech.service;

import com.chamo.chamotech.constants.MessageConstants;
import com.chamo.chamotech.dto.ApiResponse;
import com.chamo.chamotech.dto.customer.CustomerRequestDTO;
import com.chamo.chamotech.dto.customer.CustomerResponseDTO;
import com.chamo.chamotech.entity.CustomerEntity;
import com.chamo.chamotech.exception.ResourceConflictException;
import com.chamo.chamotech.exception.ResourceNotFoundException;
import com.chamo.chamotech.mapper.CustomerMapper;
import com.chamo.chamotech.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public ApiResponse<Page<CustomerResponseDTO>> getAll(Pageable pageable) {
        Page<CustomerResponseDTO> page = customerRepository.findAll(pageable)
                .map(CustomerMapper::toResponseDTO);

        return ApiResponse.success(MessageConstants.CUSTOMER_LIST, page);
    }

    public ApiResponse<CustomerResponseDTO> getById(Long id) {
        CustomerEntity customer = findCustomerOrThrow(id);
        return ApiResponse.success(MessageConstants.CUSTOMER_FOUND, CustomerMapper.toResponseDTO(customer));
    }

    public ApiResponse<CustomerResponseDTO> create(CustomerRequestDTO request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException(MessageConstants.CUSTOMER_EMAIL_EXISTS);
        }

        CustomerEntity customer = new CustomerEntity();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customerRepository.save(customer);

        return ApiResponse.success(MessageConstants.CUSTOMER_CREATED, CustomerMapper.toResponseDTO(customer));
    }

    public ApiResponse<CustomerResponseDTO> update(Long id, CustomerRequestDTO request) {
        CustomerEntity customer = findCustomerOrThrow(id);

        if (!customer.getEmail().equals(request.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException(MessageConstants.CUSTOMER_EMAIL_EXISTS);
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customerRepository.save(customer);

        return ApiResponse.success(MessageConstants.CUSTOMER_UPDATED, CustomerMapper.toResponseDTO(customer));
    }

    public ApiResponse<Void> delete(Long id) {
        CustomerEntity customer = findCustomerOrThrow(id);
        customerRepository.delete(customer);
        return ApiResponse.success(MessageConstants.CUSTOMER_DELETED, null);
    }

    private CustomerEntity findCustomerOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CUSTOMER_NOT_FOUND));
    }
}
