package com.chamo.chamotech.mapper;

import com.chamo.chamotech.dto.tag.TagResponseDTO;
import com.chamo.chamotech.entity.TagEntity;

public class TagMapper {

    private TagMapper() {
    }

    public static TagResponseDTO toResponseDTO(TagEntity tag) {
        TagResponseDTO dto = new TagResponseDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
