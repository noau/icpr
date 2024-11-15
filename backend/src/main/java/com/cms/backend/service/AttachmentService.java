package com.cms.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.pojo.Attachment;

import java.util.List;

public interface AttachmentService extends IService<Attachment> {

    // 自定义查询方法，根据 assignmentId 查找附件
    List<Attachment> findByAssignmentId(Integer assignmentId);

}
