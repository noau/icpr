package com.cms.backend.pojo.Assignments;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @TableField(exist = false)
    private Integer submitted; // 已提交数量

    @TableField(exist = false)
    private Integer submitTotal; // 总提交数量
}
