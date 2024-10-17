package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeachingDTO {
    private Integer teacherId;
    private String courseId;

    public TeachingDTO() {}
}
