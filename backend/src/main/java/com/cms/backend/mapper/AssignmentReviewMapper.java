package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Assignments.AssignmentReview;
import org.apache.ibatis.annotations.Select;

public interface AssignmentReviewMapper extends BaseMapper<AssignmentReview> {

    @Select("SELECT grade FROM assignment_review WHERE submission_id = #{id}")
    Float findById(Integer id);
    @Select("SELECT * FROM assignment_review WHERE submission_id = #{id}")
    AssignmentReview findAllBySubmissionId(Integer assignmentId);
}
