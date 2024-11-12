package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Assignments.AssignmentPeerReview;
import org.apache.ibatis.annotations.Select;

public interface AssignmentPeerReviewMapper extends BaseMapper<AssignmentPeerReview> {
    @Select("""
            SELECT COUNT(*)
            FROM assignment_submission as s,
                 assignment_peer_review as pr
            WHERE s.assignment_id = #{assignmentId}
              AND pr.reviewer_id = #{studentId}
              AND s.id = pr.submission_id""")
    int getPeerReviewCount(Integer studentId, Integer assignmentId);
}
