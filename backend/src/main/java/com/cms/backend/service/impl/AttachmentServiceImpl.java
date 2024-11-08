package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AttachmentMapper;
import com.cms.backend.pojo.Attachment;
import com.cms.backend.service.AttachmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {
    private final AttachmentMapper attachmentMapper;

    public AttachmentServiceImpl(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    public List<Attachment> findByAssignmentId(Integer assignmentId) {
        return attachmentMapper.findByAssignmentId(assignmentId);
    }
}
