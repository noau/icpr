package com.cms.backend.service.assignment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.AssignmentMapper;
import com.cms.backend.pojo.Assignments.Assignment;
import com.cms.backend.service.assignment.AssignmentService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl extends ServiceImpl<AssignmentMapper, Assignment> implements AssignmentService {

    private final AssignmentMapper assignmentMapper;

    public AssignmentServiceImpl(AssignmentMapper assignmentMapper) {
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    public String getAssignmentDescription(Integer id) {
        return assignmentMapper.selectById(id).getDescription();
    }
}
