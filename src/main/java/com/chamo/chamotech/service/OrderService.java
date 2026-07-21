package com.chamo.chamotech.service;

import com.chamo.chamotech.constants.MessageConstants;
import com.chamo.chamotech.dto.ApiResponse;
import com.chamo.chamotech.dto.order.OrderItemDTO;
import com.chamo.chamotech.dto.order.OrderRequestDTO;
import com.chamo.chamotech.dto.order.OrderResponseDTO;
import com.chamo.chamotech.entity.CustomerEntity;
import com.chamo.chamotech.entity.OrderEntity;
import com.chamo.chamotech.entity.OrderLineEntity;
import com.chamo.chamotech.entity.ProductEntity;
import com.chamo.chamotech.enums.OrderStatus;
import com.chamo.chamotech.exception.ResourceConflictException;
import com.chamo.chamotech.exception.ResourceNotFoundException;
import com.chamo.chamotech.mapper.OrderMapper;
import com.chamo.chamotech.repository.CustomerRepository;
import com.chamo.chamotech.repository.OrderRepository;
import com.chamo.chamotech.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public ApiResponse<Page<OrderResponseDTO>> getAll(Pageable pageable) {
        Page<OrderResponseDTO> page = orderRepository.findAll(pageable)
                .map(OrderMapper::toResponseDTO);

        return ApiResponse.success(MessageConstants.ORDER_LIST, page);
    }

    public ApiResponse<OrderResponseDTO> getById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ORDER_NOT_FOUND));

        return ApiResponse.success(MessageConstants.ORDER_FOUND, OrderMapper.toResponseDTO(order));
    }

    @Transactional
    public ApiResponse<OrderResponseDTO> create(OrderRequestDTO request) {
        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CUSTOMER_NOT_FOUND));

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO item : request.getItems()) {
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND));

            if (product.getStock() < item.getQuantity()) {
                throw new ResourceConflictException(MessageConstants.PRODUCT_OUT_OF_STOCK + product.getName());
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

            OrderLineEntity line = new OrderLineEntity();
            line.setOrder(order);
            line.setProduct(product);
            line.setQuantity(item.getQuantity());
            line.setUnitPrice(unitPrice);
            line.setSubtotal(subtotal);
            order.getLines().add(line);

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            total = total.add(subtotal);
        }

        order.setTotal(total);
        orderRepository.save(order);

        return ApiResponse.success(MessageConstants.ORDER_CREATED, OrderMapper.toResponseDTO(order));
    }
}
