package com.chamo.chamotech.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagRequestDTO {

    @NotBlank(message = "name is required")
    private String name;
}
