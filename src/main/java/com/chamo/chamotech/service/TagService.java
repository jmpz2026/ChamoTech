package com.chamo.chamotech.service;

import com.chamo.chamotech.constants.MessageConstants;
import com.chamo.chamotech.dto.ApiResponse;
import com.chamo.chamotech.dto.tag.TagRequestDTO;
import com.chamo.chamotech.dto.tag.TagResponseDTO;
import com.chamo.chamotech.entity.TagEntity;
import com.chamo.chamotech.exception.ResourceConflictException;
import com.chamo.chamotech.exception.ResourceNotFoundException;
import com.chamo.chamotech.mapper.TagMapper;
import com.chamo.chamotech.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public ApiResponse<Page<TagResponseDTO>> getAll(Pageable pageable) {
        Page<TagResponseDTO> page = tagRepository.findAll(pageable)
                .map(TagMapper::toResponseDTO);

        return ApiResponse.success(MessageConstants.TAG_LIST, page);
    }

    public ApiResponse<TagResponseDTO> getById(Long id) {
        TagEntity tag = findTagOrThrow(id);
        return ApiResponse.success(MessageConstants.TAG_FOUND, TagMapper.toResponseDTO(tag));
    }

    public ApiResponse<TagResponseDTO> create(TagRequestDTO request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new ResourceConflictException(MessageConstants.TAG_NAME_EXISTS);
        }

        TagEntity tag = new TagEntity();
        tag.setName(request.getName());
        tagRepository.save(tag);

        return ApiResponse.success(MessageConstants.TAG_CREATED, TagMapper.toResponseDTO(tag));
    }

    public ApiResponse<TagResponseDTO> update(Long id, TagRequestDTO request) {
        TagEntity tag = findTagOrThrow(id);

        if (!tag.getName().equals(request.getName()) && tagRepository.existsByName(request.getName())) {
            throw new ResourceConflictException(MessageConstants.TAG_NAME_EXISTS);
        }

        tag.setName(request.getName());
        tagRepository.save(tag);

        return ApiResponse.success(MessageConstants.TAG_UPDATED, TagMapper.toResponseDTO(tag));
    }

    public ApiResponse<Void> delete(Long id) {
        TagEntity tag = findTagOrThrow(id);
        tagRepository.delete(tag);
        return ApiResponse.success(MessageConstants.TAG_DELETED, null);
    }

    private TagEntity findTagOrThrow(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.TAG_NOT_FOUND));
    }
}
