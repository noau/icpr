package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.DiscussionLikeMapper;
import com.cms.backend.pojo.DiscussionLike;
import com.cms.backend.service.DiscussionLikeService;
import org.springframework.stereotype.Service;

@Service
public class DiscussionLikeServiceImpl extends ServiceImpl<DiscussionLikeMapper, DiscussionLike> implements DiscussionLikeService {
}
