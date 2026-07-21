package com.chamo.chamotech.service;

import com.chamo.chamotech.constants.MessageConstants;
import com.chamo.chamotech.dto.ApiResponse;
import com.chamo.chamotech.dto.category.CategoryRequestDTO;
import com.chamo.chamotech.dto.category.CategoryResponseDTO;
import com.chamo.chamotech.entity.CategoryEntity;
import com.chamo.chamotech.exception.ResourceConflictException;
import com.chamo.chamotech.exception.ResourceNotFoundException;
import com.chamo.chamotech.mapper.CategoryMapper;
import com.chamo.chamotech.repository.CategoryRepository;
import com.chamo.chamotech.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ApiResponse<Page<CategoryResponseDTO>> getAll(Pageable pageable) {
        Page<CategoryResponseDTO> page = categoryRepository.findAll(pageable)
                .map(CategoryMapper::toResponseDTO);

        return ApiResponse.success(MessageConstants.CATEGORY_LIST, page);
    }

    public ApiResponse<CategoryResponseDTO> getById(Long id) {
        CategoryEntity category = findCategoryOrThrow(id);
        return ApiResponse.success(MessageConstants.CATEGORY_FOUND, CategoryMapper.toResponseDTO(category));
    }

    public ApiResponse<CategoryResponseDTO> create(CategoryRequestDTO request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ResourceConflictException(MessageConstants.CATEGORY_NAME_EXISTS);
        }

        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        categoryRepository.save(category);

        return ApiResponse.success(MessageConstants.CATEGORY_CREATED, CategoryMapper.toResponseDTO(category));
    }

    public ApiResponse<CategoryResponseDTO> update(Long id, CategoryRequestDTO request) {
        CategoryEntity category = findCategoryOrThrow(id);

        if (!category.getName().equals(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new ResourceConflictException(MessageConstants.CATEGORY_NAME_EXISTS);
        }

        category.setName(request.getName());
        categoryRepository.save(category);

        return ApiResponse.success(MessageConstants.CATEGORY_UPDATED, CategoryMapper.toResponseDTO(category));
    }

    public ApiResponse<Void> delete(Long id) {
        CategoryEntity category = findCategoryOrThrow(id);

        if (productRepository.existsByCategoryId(id)) {
            throw new ResourceConflictException(MessageConstants.CATEGORY_HAS_PRODUCTS);
        }

        categoryRepository.delete(category);
        return ApiResponse.success(MessageConstants.CATEGORY_DELETED, null);
    }

    private CategoryEntity findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
    }
}
