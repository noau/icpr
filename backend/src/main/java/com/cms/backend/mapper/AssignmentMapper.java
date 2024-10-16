package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Assignments.Assignment;
import org.apache.ibatis.annotations.Update;

public interface AssignmentMapper extends BaseMapper<Assignment> {
    /**
     * 发布作业附件
     *
     * @param attachmentId 附件ID
     * @param assignmentId 作业ID
     */
    @Update("update attachment set assignment_id = #{assignmentId} where id = #{attachmentId}")
    void findAssignmentAttachments(Integer attachmentId, Integer assignmentId);
}
