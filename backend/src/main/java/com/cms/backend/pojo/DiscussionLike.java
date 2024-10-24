package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscussionLike {

    @TableId
    private Integer id;

    private Integer userId;

    private Integer threadId;

    private String courseId;

    private Integer replyId;

    private String createdAt;

}
