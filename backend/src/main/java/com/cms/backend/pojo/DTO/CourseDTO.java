package com.cms.backend.pojo.DTO;

import com.cms.backend.pojo.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseDTO {

    private List<Course> courses;

    public CourseDTO(List<Course> courses) {
        this.courses = courses;
    }

}
