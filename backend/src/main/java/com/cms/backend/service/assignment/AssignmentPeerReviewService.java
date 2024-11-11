package com.cms.backend.service.assignment;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.pojo.Assignments.AssignmentPeerReview;


public interface AssignmentPeerReviewService extends IService<AssignmentPeerReview> {
    int getPeerReviewCount(Integer studentId, Integer assignmentId);
}
