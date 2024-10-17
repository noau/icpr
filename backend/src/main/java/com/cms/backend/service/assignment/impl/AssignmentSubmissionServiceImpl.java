package com.cms.backend.service.assignment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AssignmentSubmissionMapper;
import com.cms.backend.pojo.Assignments.AssignmentSubmission;
import com.cms.backend.service.assignment.AssignmentSubmissionService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentSubmissionServiceImpl extends ServiceImpl<AssignmentSubmissionMapper, AssignmentSubmission> implements AssignmentSubmissionService {
}
