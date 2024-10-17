package com.cms.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.Teaching;
import com.cms.backend.pojo.User;

import java.util.List;

public interface CourseService extends IService<Course> {
    List<Course> getAllCourse(Integer id);

    List<User> getAllStudents(String id);

    void addCourse(String id, String courseNumber, String name, String semesterYear, Integer classNumber, String start, String end, String academy, String teacher);

    void addStudentCourseSelection(Integer studentId, String courseId);

    void uploadResourceExam(String courseId, Integer attachmentId);

    void uploadResourcePpt(String courseId, Integer attachmentId);

    void uploadResourceExercise(String courseId, Integer attachmentId);

    Teaching getTeacher(String id);

    List<Integer> selectSubmission(Integer id);

    Float selectGrade(Integer submissionId);

    void uploadResourceSyllabus(String courseId, Integer attachmentId);

    void uploadResourceCalendar(String courseId, Integer attachmentId);

}
