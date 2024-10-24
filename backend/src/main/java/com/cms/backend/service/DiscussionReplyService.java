package com.cms.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.pojo.DiscussionReply;

public interface DiscussionReplyService extends IService<DiscussionReply> {
    void createReply(Integer replyId, Integer userId, String content, String createdAt);

}
