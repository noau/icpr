package com.cms.backend.service.assignment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AssignmentReviewMapper;
import com.cms.backend.pojo.Assignments.AssignmentReview;
import com.cms.backend.service.assignment.AssignmentReviewService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentReviewServiceImpl extends ServiceImpl<AssignmentReviewMapper, AssignmentReview> implements AssignmentReviewService {
}
