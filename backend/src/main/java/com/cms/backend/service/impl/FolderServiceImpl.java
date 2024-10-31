package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.FolderMapper;
import com.cms.backend.pojo.Folder;
import com.cms.backend.service.FolderService;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {
}
