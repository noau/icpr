package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notification {

    @TableId(type = IdType.AUTO)  // 设置 ID 自增
    private Integer id;

    private Integer userId;

    private String title;

    private Integer triggeredBy;

    private String type;

    private Integer relatedId;

    private String content;

    private Integer isRead;

    private String createdAt;

    private String courseId;

    private Integer isStar;

    public Notification(Integer userId, String title, Integer triggeredBy, String type, Integer relatedId, String content, Integer isRead, String createdAt, String courseId, Integer isStar) {
        this.userId = userId;
        this.title = title;
        this.triggeredBy = triggeredBy;
        this.type = type;
        this.relatedId = relatedId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.courseId = courseId;
        this.isStar = isStar;
    }

}
