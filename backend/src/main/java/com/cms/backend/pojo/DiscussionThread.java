package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscussionThread {

    @TableId(type = IdType.AUTO)  // 设置 ID 自增
    private Integer id;

    private String courseId;

    private Integer userId;

    private String title;

    private String content;

    private Integer likes;

    private Integer favorites;

    private Integer closed;

    private Integer top;

    private String createdAt;

    private String tag;

    private String updatedAt;

}
