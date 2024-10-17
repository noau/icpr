package com.cms.backend.pojo.Assignments;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignmentSubmission {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer assignmentId;

    private Integer studentId;

    private String submittedAt;

    private String content;
}
