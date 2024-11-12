package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.controller.CourseController;
import com.cms.backend.mapper.AttachmentFolderMapper;
import com.cms.backend.service.AttachmentFolderService;
import org.springframework.stereotype.Service;

@Service
public class AttachmentFolderServiceImpl extends ServiceImpl<AttachmentFolderMapper, CourseController.AttachmentFolder> implements AttachmentFolderService {
}
