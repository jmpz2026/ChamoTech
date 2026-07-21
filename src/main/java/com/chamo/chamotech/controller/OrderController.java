package com.chamo.chamotech.controller;

import com.chamo.chamotech.dto.ApiResponse;
import com.chamo.chamotech.dto.order.OrderRequestDTO;
import com.chamo.chamotech.dto.order.OrderResponseDTO;
import com.chamo.chamotech.enums.Role;
import com.chamo.chamotech.security.RequiresRole;
import com.chamo.chamotech.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @RequiresRole(Role.ADMIN)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(orderService.getAll(pageable));
    }

    @RequiresRole(Role.ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> create(@RequestBody @Valid OrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }
}
