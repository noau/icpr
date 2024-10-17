package com.cms.backend.pojo.Assignments;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Assignment {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String courseId;

    private String title;

    private String description;

    private String start;

    private String end;

    private Integer isPrivate;

    private Float fullGrade;

    private Float delayedGrade;

    private String latestEnd;

    private Integer multipleSubmission;

    private Integer publishGrade;

    private Integer requirePeerReview;

    private String peerReviewStart;

    private String peerReviewEnd;

    private Integer minPeerReview;

    private String answer;
}
