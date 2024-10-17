package com.cms.backend.pojo.Assignments;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AssignmentSubmission {

    @TableId
    private Integer id;

    private Integer assignmentId;

    private Integer studentId;

    private String submittedAt;

    private String content;

}
