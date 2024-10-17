package com.cms.backend.pojo.DTO.Assignment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DescriptionDTO {
    private String description;

    public DescriptionDTO(String description) {
        this.description = description;
    }
}
