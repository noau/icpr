package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Attachment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AttachmentMapper extends BaseMapper<Attachment> {
    @Select("SELECT * FROM attachment WHERE assignment_id = #{assignmentId}")
    List<Attachment> findByAssignmentId(Integer assignmentId);
}
