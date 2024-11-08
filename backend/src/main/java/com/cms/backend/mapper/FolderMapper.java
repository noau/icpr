package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.controller.CourseController;
import com.cms.backend.pojo.Folder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FolderMapper extends BaseMapper<Folder> {
    @Select("select * from attachment_folder")
     List<CourseController.AttachmentFolder> getFolderlist(String id);
}


