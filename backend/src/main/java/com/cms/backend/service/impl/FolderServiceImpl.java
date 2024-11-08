package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.controller.CourseController;
import com.cms.backend.mapper.CourseMapper;
import com.cms.backend.mapper.FolderMapper;
import com.cms.backend.pojo.Folder;
import com.cms.backend.service.FolderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {
    private final FolderMapper folderMapper;

    public FolderServiceImpl(FolderMapper folderMapper) {
        this.folderMapper = folderMapper;
    }

    @Override
    public List<CourseController.AttachmentFolder> getFolderlist(String id) {
        return folderMapper.getFolderlist(id);
    }
}
