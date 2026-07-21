package com.chamo.chamotech.service;

import com.chamo.chamotech.constants.MessageConstants;
import com.chamo.chamotech.dto.ApiResponse;
import com.chamo.chamotech.dto.product.ProductRequestDTO;
import com.chamo.chamotech.dto.product.ProductResponseDTO;
import com.chamo.chamotech.entity.CategoryEntity;
import com.chamo.chamotech.entity.ProductEntity;
import com.chamo.chamotech.entity.TagEntity;
import com.chamo.chamotech.exception.ResourceConflictException;
import com.chamo.chamotech.exception.ResourceNotFoundException;
import com.chamo.chamotech.mapper.ProductMapper;
import com.chamo.chamotech.repository.CategoryRepository;
import com.chamo.chamotech.repository.OrderLineRepository;
import com.chamo.chamotech.repository.ProductRepository;
import com.chamo.chamotech.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final OrderLineRepository orderLineRepository;

    public ApiResponse<Page<ProductResponseDTO>> getAll(Pageable pageable) {
        Page<ProductResponseDTO> page = productRepository.findAll(pageable)
                .map(ProductMapper::toResponseDTO);

        return ApiResponse.success(MessageConstants.PRODUCT_LIST, page);
    }

    public ApiResponse<ProductResponseDTO> getById(Long id) {
        ProductEntity product = findProductOrThrow(id);
        return ApiResponse.success(MessageConstants.PRODUCT_FOUND, ProductMapper.toResponseDTO(product));
    }

    public ApiResponse<Page<ProductResponseDTO>> search(Long categoryId, Pageable pageable) {
        Page<ProductEntity> result = (categoryId == null)
                ? productRepository.findAll(pageable)
                : productRepository.findByCategoryId(categoryId, pageable);

        Page<ProductResponseDTO> page = result.map(ProductMapper::toResponseDTO);

        return ApiResponse.success(MessageConstants.PRODUCT_LIST, page);
    }

    public ApiResponse<ProductResponseDTO> create(ProductRequestDTO request) {
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));

        ProductEntity product = new ProductEntity();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);
        product.setTags(resolveTags(request.getTagIds()));
        productRepository.save(product);

        return ApiResponse.success(MessageConstants.PRODUCT_CREATED, ProductMapper.toResponseDTO(product));
    }

    public ApiResponse<ProductResponseDTO> update(Long id, ProductRequestDTO request) {
        ProductEntity product = findProductOrThrow(id);

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);
        product.setTags(resolveTags(request.getTagIds()));
        productRepository.save(product);

        return ApiResponse.success(MessageConstants.PRODUCT_UPDATED, ProductMapper.toResponseDTO(product));
    }

    public ApiResponse<Void> delete(Long id) {
        ProductEntity product = findProductOrThrow(id);

        if (orderLineRepository.existsByProductId(id)) {
            throw new ResourceConflictException(MessageConstants.PRODUCT_IN_ORDERS);
        }

        productRepository.delete(product);
        return ApiResponse.success(MessageConstants.PRODUCT_DELETED, null);
    }

    private List<TagEntity> resolveTags(List<Long> tagIds) {
        List<TagEntity> tags = new ArrayList<>();
        if (tagIds == null) {
            return tags;
        }
        for (Long tagId : tagIds) {
            TagEntity tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.TAG_NOT_FOUND));
            tags.add(tag);
        }
        return tags;
    }

    private ProductEntity findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND));
    }
}
