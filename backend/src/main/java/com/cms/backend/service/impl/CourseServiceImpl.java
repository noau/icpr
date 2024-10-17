package com.cms.backend.service.impl;

import com.cms.backend.mapper.CourseMapper;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.User;
import com.cms.backend.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public List<Course> getAllCourse(Integer id) {
        return courseMapper.getAllCourse(id);
    }

    @Override
    public List<User> getAllStudents(String id) {
        return courseMapper.getAllStudents(id);
    }

    @Override
    public void addCourse(String id, String courseNumber, String name, String semesterYear, Integer classNumber, String start, String end, String academy, String teacher) {
        courseMapper.addCourse(id, courseNumber, name, semesterYear, classNumber, start, end, academy,teacher);
    }

    @Override
    public void addStudentCourseSelection(Integer studentId, String courseId) {
        courseMapper.addStudentCourseSelection(studentId, courseId);
    }

    @Override
    public void addTeaching(Integer teacherId, String courseId) {
        courseMapper.addTeaching(teacherId,courseId);
    }


}
