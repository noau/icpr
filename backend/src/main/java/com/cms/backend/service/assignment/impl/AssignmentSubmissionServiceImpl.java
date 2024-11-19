package com.cms.backend.service.assignment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AssignmentSubmissionMapper;
import com.cms.backend.pojo.Assignments.AssignmentSubmission;
import com.cms.backend.service.assignment.AssignmentSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssignmentSubmissionServiceImpl extends ServiceImpl<AssignmentSubmissionMapper, AssignmentSubmission> implements AssignmentSubmissionService {
    private AssignmentSubmissionMapper mapper;

    @Override
    public List<AssignmentSubmission> getPeerReviewSubmissions(Integer assignmentId, Integer reviewerId) {
        return mapper.getPeerReviewSubmissions(assignmentId, reviewerId);
    }
}
