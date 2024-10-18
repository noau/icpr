package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourseSelectionDTO {

    private Integer studentId;

    private String courseId;

    public StudentCourseSelectionDTO() {}
}
