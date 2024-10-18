package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AttachmentMapper;
import com.cms.backend.pojo.Attachment;
import com.cms.backend.service.AttachmentService;
import org.springframework.stereotype.Service;

@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {
}
