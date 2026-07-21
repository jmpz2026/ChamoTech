package com.chamo.chamotech.controller;

import com.chamo.chamotech.dto.ApiResponse;
import com.chamo.chamotech.dto.customer.CustomerRequestDTO;
import com.chamo.chamotech.dto.customer.CustomerResponseDTO;
import com.chamo.chamotech.enums.Role;
import com.chamo.chamotech.security.RequiresRole;
import com.chamo.chamotech.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponseDTO>>> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(customerService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @RequiresRole(Role.ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> create(@RequestBody @Valid CustomerRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request));
    }

    @RequiresRole(Role.ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> update(@PathVariable Long id, @RequestBody @Valid CustomerRequestDTO request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @RequiresRole(Role.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.delete(id));
    }
}
