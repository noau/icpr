package com.cms.backend.service.assignment;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.pojo.Assignments.AssignmentReview;

public interface AssignmentReviewService extends IService<AssignmentReview> {

    Float findById(Integer id);

    AssignmentReview findAllBySubmissionId(Integer assignmentId);

    Integer getPeerReviewByStudentId(Integer studentId, Integer assignmentId);
}
