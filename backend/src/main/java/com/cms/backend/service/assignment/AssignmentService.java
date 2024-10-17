package com.cms.backend.service.assignment;


import com.baomidou.mybatisplus.extension.service.IService;

import com.cms.backend.pojo.Assignments.Assignment;

public interface AssignmentService extends IService<Assignment> {

    String getAssignmentDescription(Integer id);
}

