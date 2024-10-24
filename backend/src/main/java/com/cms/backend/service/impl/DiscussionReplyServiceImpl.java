package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.DiscussionReplyMapper;
import com.cms.backend.pojo.DiscussionReply;
import com.cms.backend.service.DiscussionReplyService;
import org.springframework.stereotype.Service;

@Service
public class DiscussionReplyServiceImpl extends ServiceImpl<DiscussionReplyMapper, DiscussionReply> implements DiscussionReplyService {

    private final DiscussionReplyMapper discussionReplyMapper;

    public DiscussionReplyServiceImpl(DiscussionReplyMapper discussionReplyMapper) {
        this.discussionReplyMapper = discussionReplyMapper;
    }

    @Override
    public void createReply(Integer replyId, Integer userId, String content, String createdAt) {
        discussionReplyMapper.createReply(replyId, userId, content, createdAt);
    }
}
