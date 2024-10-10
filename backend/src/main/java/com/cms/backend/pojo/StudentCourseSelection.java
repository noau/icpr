package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "student_course_selection")
public class StudentCourseSelection {

    @EmbeddedId
    private StudentCourseSelectionKey id;

    @Column(name = "grade", length = 255)
    private String grade;

}