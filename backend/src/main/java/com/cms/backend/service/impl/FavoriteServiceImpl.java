package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.FavoriteMapper;
import com.cms.backend.mapper.FolderMapper;
import com.cms.backend.pojo.Favorite;
import com.cms.backend.pojo.Folder;
import com.cms.backend.service.FavoriteService;
import com.cms.backend.service.FolderService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

}
