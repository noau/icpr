package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.DiscussionReply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiscussionReplyMapper extends BaseMapper<DiscussionReply> {

    @Insert("insert into discussion_reply (reply_id, user_id, content, created_at, replied_id) values (#{replyId}, #{userId}, #{content}, #{createdAt}, #{repliedId})")
    void createReply(Integer replyId, Integer userId, String content, String createdAt, Integer repliedId);

}
