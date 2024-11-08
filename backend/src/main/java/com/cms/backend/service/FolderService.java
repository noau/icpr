package com.cms.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.controller.CourseController;
import com.cms.backend.pojo.Folder;

import java.util.List;

public interface FolderService extends IService<Folder> {
    List<CourseController.AttachmentFolder> getFolderlist(String id);
}
