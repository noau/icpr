package com.cms.backend.pojo;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

@Data
public class StudentCourseSelection {

    @MppMultiId
    private Integer studentId;

    @MppMultiId
    private String courseId;

    private Float grade;

}