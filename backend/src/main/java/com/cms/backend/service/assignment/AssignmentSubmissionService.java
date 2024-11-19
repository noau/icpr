package com.cms.backend.service.assignment;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.pojo.Assignments.AssignmentSubmission;

import java.util.List;

public interface AssignmentSubmissionService extends IService<AssignmentSubmission> {
    List<AssignmentSubmission> getPeerReviewSubmissions(Integer assignmentId, Integer reviewerId);
}
