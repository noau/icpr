package com.cms.backend.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.Assignments.AssignmentReview;
import com.cms.backend.pojo.Attachment;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.pojo.TeacherInfo;
import com.cms.backend.pojo.User;
import com.cms.backend.service.AttachmentService;
import com.cms.backend.service.CourseService;
import com.cms.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    private final AttachmentService attachmentService;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public CourseController(CourseService courseService, AttachmentService attachmentService, UserService userService) {
        this.courseService = courseService;
        this.attachmentService = attachmentService;
        this.userService = userService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<CourseList> getAllCourse(@RequestParam Integer id) {
        List<Course> courses = courseService.getAllCourse(id);
        CourseList courseList = new CourseList(courses);

        return ResponseEntity.ok(courseList);
    }

    @GetMapping(value = "/all-students")
    public ResponseEntity<StudentList> getAllStudents(@RequestParam String id) {
        List<User> students = courseService.getAllStudents(id);
        StudentList studentList = new StudentList(students);

        return ResponseEntity.ok(studentList);
    }

    @PostMapping(value = "/export-student-list")
    public ResponseEntity<ExportStudentList> exportStudentList(@RequestParam String id) {
        // 获取课程的所有学生列表
        List<User> students = courseService.getAllStudents(id);

        // 将 User 对象转换为 Student 对象
        List<Student> studentList = students.stream()
                .map(user -> new Student(user.getId(), user.getName()))
                .collect(Collectors.toList());

        // 定义文件的保存路径
        String fileName = "D:\\nginx\\nginx-1.26.2\\ICPRFiles\\" + id + "学生列表" + ".xlsx";

        // 使用 EasyExcel 写入文件，这里使用 Student 作为 Excel 数据模型
        EasyExcel.write(fileName, Student.class).sheet("学生信息").doWrite(studentList);

        // 定义文件在服务器上的 URL
        String url = "http://localhost:65/" + id + "学生列表" + ".xlsx";

        // 返回课程ID和文件的URL
        return ResponseEntity.ok(new ExportStudentList(id, url));
    }




    @PostMapping(value = "/resource-exam")
    public ResponseEntity<String> uploadResourceExam(@RequestBody CourseResources courseResources) {
        List<Integer> attachments = courseResources.getAttachmentIdList();
        for (var attachmentId : attachments) {
            courseService.uploadResourceExam(courseResources.getId(), attachmentId);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/resource-ppt")
    public ResponseEntity<String> uploadResourcePpt(@RequestBody CourseResources courseResources) {
        List<Integer> attachments = courseResources.getAttachmentIdList();
        for (var attachmentId : attachments) {
            courseService.uploadResourcePpt(courseResources.getId(), attachmentId);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/resource-exercise")
    public ResponseEntity<String> uploadResourceExercise(@RequestBody CourseResources courseResources) {
        List<Integer> attachments = courseResources.getAttachmentIdList();
        for (var attachmentId : attachments) {
            courseService.uploadResourceExercise(courseResources.getId(), attachmentId);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/resource-syllabus")
    public ResponseEntity<String> uploadResourceSyllabus(@RequestBody CourseResources courseResources) {
        List<Integer> attachments = courseResources.getAttachmentIdList();
        for (var attachmentId : attachments) {
            courseService.uploadResourceSyllabus(courseResources.getId(), attachmentId);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/resource-calendar")
    public ResponseEntity<String> uploadResourceCalendar(@RequestBody CourseResources courseResources) {
        List<Integer> attachments = courseResources.getAttachmentIdList();
        for (var attachmentId : attachments) {
            courseService.uploadResourceCalendar(courseResources.getId(), attachmentId);
        }

        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/get-info")
    public ResponseEntity<Course> getCourseInfo(@RequestParam String id) {
        Course course = courseService.getById(id);

        return ResponseEntity.ok(course);
    }

    @GetMapping(value = "/get-syllabus")
    public ResponseEntity<AttachmentIdList> getSyllabus(@RequestParam String id) {
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getSyllabusId, id));
        AttachmentIdList attachmentIdList = new AttachmentIdList(attachmentList);

        return ResponseEntity.ok(attachmentIdList);
    }

    @GetMapping(value = "/get-calendar")
    public ResponseEntity<AttachmentIdList> getCalendar(@RequestParam String id) {
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getCalendarId, id));
        AttachmentIdList attachmentIdList = new AttachmentIdList(attachmentList);

        return ResponseEntity.ok(attachmentIdList);
    }

    @GetMapping(value = "/get-teacher")
    public ResponseEntity<Teacher> getTeacher(@RequestParam String id) {
        TeachingDTO teachingDTO = courseService.getTeacherId(id);
        User user = userService.findByUserName(teachingDTO.getTeacherId());
        TeacherInfo teacherInfo = courseService.getTeacherInfo(teachingDTO.getTeacherId());
        Teacher teacher = new Teacher(user.getName(), user.getAcademy(), user.getGender(), user.getAcademy(), user.getEmail(), user.getPhoneNumber(), teacherInfo.getAddress(), teacherInfo.getTitle(), teacherInfo.getBrief());

        return ResponseEntity.ok(teacher);
    }

    @GetMapping(value = "/get-exam")
    public ResponseEntity<AttachmentIdList> getExam(@RequestParam String id) {
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getExamId, id));
        AttachmentIdList attachmentIdList = new AttachmentIdList(attachmentList);

        return ResponseEntity.ok(attachmentIdList);
    }

    @GetMapping(value = "/get-ppt")
    public ResponseEntity<AttachmentIdList> getPpt(@RequestParam String id) {
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getPptId, id));
        AttachmentIdList attachmentIdList = new AttachmentIdList(attachmentList);

        return ResponseEntity.ok(attachmentIdList);
    }

    @GetMapping(value = "/get-exercise")
    public ResponseEntity<AttachmentIdList> getExercise(@RequestParam String id) {
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getExerciseId, id));
        AttachmentIdList attachmentIdList = new AttachmentIdList(attachmentList);

        return ResponseEntity.ok(attachmentIdList);
    }

    @GetMapping(value = "/grade-list")
    public ResponseEntity<GradeList> getGradeList(@RequestParam Integer id) {
        List<Integer> assignmentSubmissionList = courseService.selectSubmission(id);
        List<AssignmentReview> assignmentReviewList = new ArrayList<>();
        for (var submissionId : assignmentSubmissionList) {
            AssignmentReview assignmentReview = new AssignmentReview(submissionId, courseService.selectGrade(submissionId), "", "");
            assignmentReviewList.add(assignmentReview);
        }

        GradeList gradeList = new GradeList(assignmentReviewList);

        return ResponseEntity.ok(gradeList);
    }

    @Data
    @AllArgsConstructor
    public static class CourseResources {

        private String id;

        private List<Integer> attachmentIdList;

    }

    @Data
    @AllArgsConstructor
    public static class StudentList {

        private List<User> students;

    }

    @Data
    @AllArgsConstructor
    public static class Student {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class CourseList {

        private List<Course> courses;

    }

    @Data
    @AllArgsConstructor
    public static class AttachmentIdList {

        private List<Attachment> attachmentIdList;

    }

    @Data
    @AllArgsConstructor
    public static class Teacher {

        private String name;

        private String academy;

        private String gender;

        private String avatar;

        private String email;

        private String phoneNumber;

        private String address;

        private String title;

        private String brief;

    }

    @Data
    @AllArgsConstructor
    public static class GradeList {

        private List<AssignmentReview> gradeList;

    }

    @Data
    @AllArgsConstructor
    public static class ExportStudentList {
        private String id;
        private String url;
    }
}
