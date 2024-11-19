package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Assignments.AssignmentSubmission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AssignmentSubmissionMapper extends BaseMapper<AssignmentSubmission> {
    @Select("""
            SELECT s.*
            FROM assignment_submission as s,
                 user
            WHERE s.student_id = user.id
              AND s.assignment_id = #{assignmentId}
              AND NOT s.student_id = #{reviewerId}
            ORDER BY user.mark""")
    List<AssignmentSubmission> getPeerReviewSubmissions(Integer assignmentId, Integer reviewerId);
}
