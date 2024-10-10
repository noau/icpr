package com.cms.backend.service;

import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.User;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourse(Integer id);

    List<User> getAllStudents(String id);
}
