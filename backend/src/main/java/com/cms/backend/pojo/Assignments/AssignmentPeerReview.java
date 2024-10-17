package com.cms.backend.pojo.Assignments;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AssignmentPeerReview {
    @TableId
    private Integer submissionId;
    private Integer reviewerId;


    private Float grade;


    private String feedback;


    private String reviewedAt;
}
