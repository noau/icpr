package com.cms.backend.pojo.Assignments;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignmentReview {

    @TableId
    private Integer submissionId;

    private Float grade;

    private String feedback;

    private String gradedAt;

}
