package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Assignments.AssignmentReview;
import org.apache.ibatis.annotations.Select;

public interface AssignmentReviewMapper extends BaseMapper<AssignmentReview> {

    @Select("SELECT grade FROM assignment_review WHERE submission_id = #{id}")
    Float findById(Integer id);
    @Select("SELECT * FROM assignment_review WHERE submission_id = #{id}")
    AssignmentReview findAllBySubmissionId(Integer assignmentId);
    @Select("SELECT count(*) FROM assignment_submission s INNER JOIN assignment_peer_review pr ON pr.submission_id = s.id WHERE pr.reviewer_id = #{studentId} AND s.assignment_id = #{assignmentId}")
    Integer getPeerReviewByStudentId(Integer studentId, Integer assignmentId);
}
