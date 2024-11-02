package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Course {

    @TableId
    private String id;

    private String courseNumber;

    private String name;

    private String semesterYear;

    private Integer classNumber;

    private String start;

    private String end;

    private String academy;

    private String teacher;

    private String introduction;

}
