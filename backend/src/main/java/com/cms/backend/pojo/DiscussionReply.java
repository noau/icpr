package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscussionReply {

    @TableId
    private Integer id;

    private Integer threadId;

    private Integer replyId;

    private Integer userId;

    private String content;

    private Integer likes;

    private String createdAt;

}
