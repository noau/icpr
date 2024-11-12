package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.controller.CourseController;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentFolderMapper extends BaseMapper<CourseController.AttachmentFolder> {
}
