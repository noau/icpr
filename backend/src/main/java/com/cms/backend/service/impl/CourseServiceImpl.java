package com.cms.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.backend.mapper.CourseMapper;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.pojo.TeacherInfo;
import com.cms.backend.pojo.User;
import com.cms.backend.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

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

    public void uploadResourceExam(String courseId, Integer attachmentId) {
        courseMapper.uploadResourceExam(courseId, attachmentId);
    }

    @Override
    public void uploadResourcePpt(String courseId, Integer attachmentId) {
        courseMapper.uploadResourcePpt(courseId, attachmentId);
    }

    @Override
    public void uploadResourceExercise(String courseId, Integer attachmentId) {
        courseMapper.uploadResourceExercise(courseId, attachmentId);
    }

    @Override
    public TeachingDTO getTeacherId(String id) {
        return courseMapper.getTeacherId(id);
    }

    @Override
    public List<Integer> selectSubmission(Integer id) {
        return courseMapper.selectSubmission(id);
    }

    @Override
    public Float selectGrade(Integer submissionId) {
        return courseMapper.selectGrade(submissionId);
    }

    @Override
    public void uploadResourceSyllabus(String courseId, Integer attachmentId) {
        courseMapper.uploadResourceSyllabus(courseId, attachmentId);
    }

    @Override
    public void uploadResourceCalendar(String courseId, Integer attachmentId) {
        courseMapper.uploadResourceCalendar(courseId, attachmentId);
    }

    @Override
    public TeacherInfo getTeacherInfo(Integer teachingId) {
        return courseMapper.getTeacherInfo(teachingId);
    }

}
