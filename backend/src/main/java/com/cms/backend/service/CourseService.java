package com.cms.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.backend.controller.CourseController;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.pojo.TeacherInfo;
import com.cms.backend.pojo.User;

import java.util.List;

public interface CourseService extends IService<Course> {
    List<Course> getAllCourse(Integer id);

    List<User> getAllStudents(String id);

    void addCourse(String id, String courseNumber, String name, String semesterYear, Integer classNumber, String start, String end, String academy, String teacher, String introduction);

    void addStudentCourseSelection(Integer studentId, String courseId);

    void addTeaching(Integer teacherId, String courseId);

    void uploadResourceExam(String courseId, Integer attachmentId, Integer allowDownload, Integer attachmentFolderId);

    void uploadResourcePpt(String courseId, Integer attachmentId, Integer allowDownload, Integer attachmentFolderId);

    void uploadResourceExercise(String courseId, Integer attachmentId, Integer allowDownload, Integer attachmentFolderId);

    TeachingDTO getTeacherId(String id);

    List<Integer> selectSubmission(Integer id);

    Float selectGrade(Integer submissionId);

    void uploadResourceSyllabus(String courseId, Integer attachmentId);

    void uploadResourceCalendar(String courseId, Integer attachmentId);

    TeacherInfo getTeacherInfo(Integer teachingId);

    void createAttachmentFolder(CourseController.AttachmentFolder attachmentFolder);

    void deleteAttachmentFolder(Integer id);

    CourseController.AttachmentFolder getAttachmentFolder(Integer attachmentFolderId);

    List<Course> getAllCourseTeacher(Integer id);

}

