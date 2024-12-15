package com.cms.backend.service.assignment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AssignmentReviewMapper;
import com.cms.backend.pojo.Assignments.AssignmentReview;
import com.cms.backend.service.assignment.AssignmentReviewService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentReviewServiceImpl extends ServiceImpl<AssignmentReviewMapper, AssignmentReview> implements AssignmentReviewService {

    private final AssignmentReviewMapper assignmentReviewMapper;

    public AssignmentReviewServiceImpl(AssignmentReviewMapper assignmentReviewMapper) {
        this.assignmentReviewMapper = assignmentReviewMapper;
    }

    @Override
    public Float findById(Integer id) {
        return assignmentReviewMapper.findById(id);
    }

    @Override
    public AssignmentReview findAllBySubmissionId(Integer assignmentId) {
        return assignmentReviewMapper.findAllBySubmissionId(assignmentId);
    }

    @Override
    public Integer getPeerReviewByStudentId(Integer studentId, Integer assignmentId) {
        return assignmentReviewMapper.getPeerReviewByStudentId(studentId, assignmentId);
    }
}
