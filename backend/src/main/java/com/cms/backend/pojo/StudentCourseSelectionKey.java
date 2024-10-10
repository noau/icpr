package com.cms.backend.pojo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class StudentCourseSelectionKey implements Serializable {

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "course_id", length = 255)
    private String courseId;

}
