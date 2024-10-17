package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Attachment {
    @TableId
    private Integer id;

    private String url;

    private String name;

    private Integer submissionId;

    private Integer assignmentId;

    private String examId;

    private String pptId;

    private String exerciseId;

    private String avatarId;

    private String answerId;
}
