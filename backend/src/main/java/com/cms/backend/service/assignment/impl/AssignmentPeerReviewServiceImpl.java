package com.cms.backend.service.assignment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AssignmentPeerReviewMapper;
import com.cms.backend.pojo.Assignments.AssignmentPeerReview;
import com.cms.backend.service.assignment.AssignmentPeerReviewService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentPeerReviewServiceImpl extends ServiceImpl<AssignmentPeerReviewMapper, AssignmentPeerReview> implements AssignmentPeerReviewService {
}
