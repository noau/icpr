package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notification {

    @TableId
    private Integer id;

    private Integer userId;

    private Integer triggeredBy;

    private String type;

    private Integer relatedId;

    private String content;

    private Integer isRead;

    private String createdAt;

    private String courseId;

    private Integer isStar;
}
