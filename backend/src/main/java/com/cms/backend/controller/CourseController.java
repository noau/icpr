package com.cms.backend.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.BackendApplication;
import com.cms.backend.pojo.*;
import com.cms.backend.pojo.Assignments.Assignment;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.service.*;
import com.cms.backend.service.assignment.AssignmentService;
import com.cms.backend.service.assignment.AssignmentSubmissionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final AttachmentFolderService attachmentFolderService;

    private final AssignmentService assignmentService;

    private final AssignmentSubmissionService assignmentSubmissionService;

    public CourseController(CourseService courseService, AttachmentService attachmentService, UserService userService, AttachmentFolderService attachmentFolderService, AssignmentService assignmentService, AssignmentSubmissionService assignmentSubmissionService) {
        this.courseService = courseService;
        this.attachmentService = attachmentService;
        this.userService = userService;
        this.attachmentFolderService = attachmentFolderService;
        this.assignmentService = assignmentService;
        this.assignmentSubmissionService = assignmentSubmissionService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<CourseList> getAllCourse(@RequestParam Integer id) {
        List<Course> courses = courseService.getAllCourse(id);
        CourseList courseList = new CourseList(courses);

        return ResponseEntity.ok(courseList);
    }

    @GetMapping(value = "/all-teach")
    public ResponseEntity<CourseList> getAllCourseTeacher(@RequestParam Integer id) {
        List<Course> courses = courseService.getAllCourseTeacher(id);
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
        String fileName = BackendApplication.NGINX_BASE_PATH + id + "学生列表" + ".xlsx";

        // 使用 EasyExcel 写入文件，这里使用 Student 作为 Excel 数据模型
        EasyExcel.write(fileName, Student.class).sheet("学生信息").doWrite(studentList);

        // 定义文件在服务器上的 URL
        String url = "http://localhost:65/" + id + "学生列表" + ".xlsx";

        // 返回课程ID和文件的URL
        return ResponseEntity.ok(new ExportStudentList(id, url));
    }

    @PostMapping(value = "/resource-exam")
    public ResponseEntity<String> uploadResourceExam(@RequestBody CourseResourcesDTO courseResources) {
        List<AttachmentResources> attachments = courseResources.getAttachmentIdList();
        for (var attachment : attachments) {
            courseService.uploadResourceExam(courseResources.getId(), attachment.getId(), attachment.getAllowDownload(), attachment.getAttachmentFolderId());
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/resource-ppt")
    public ResponseEntity<String> uploadResourcePpt(@RequestBody CourseResourcesDTO courseResources) {
        List<AttachmentResources> attachments = courseResources.getAttachmentIdList();
        for (var attachment : attachments) {
            System.out.println(courseResources.getId());
            System.out.println(attachment.getId());
            System.out.println(attachment.getAllowDownload());
            System.out.println(attachment.getAttachmentFolderId());
            courseService.uploadResourcePpt(courseResources.getId(), attachment.getId(), attachment.getAllowDownload(), attachment.getAttachmentFolderId());
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/resource-exercise")
    public ResponseEntity<String> uploadResourceExercise(@RequestBody CourseResourcesDTO courseResources) {
        List<AttachmentResources> attachments = courseResources.getAttachmentIdList();
        for (var attachment : attachments) {
            courseService.uploadResourceExercise(courseResources.getId(), attachment.getId(), attachment.getAllowDownload(), attachment.getAttachmentFolderId());
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
        User user = userService.findById(teachingDTO.getTeacherId());
        TeacherInfo teacherInfo = courseService.getTeacherInfo(teachingDTO.getTeacherId());
        Teacher teacher = new Teacher(user.getName(), user.getAcademy(), user.getGender(), user.getAvatar(), user.getEmail(), user.getPhoneNumber(), teacherInfo.getAddress(), teacherInfo.getTitle(), teacherInfo.getBrief());

        return ResponseEntity.ok(teacher);
    }

    @GetMapping(value = "/get-exam")
    public ResponseEntity<List<FolderAttachmentListDTO>> getExam(@RequestParam String id) {
        List<AttachmentFolder> folders = attachmentFolderService.list(new LambdaQueryWrapper<AttachmentFolder>().eq(AttachmentFolder::getCourseId, id).eq(AttachmentFolder::getType, "exam"));
        List<FolderAttachmentListDTO> folderAttachmentListDTOS = new ArrayList<>();

        for (AttachmentFolder folder : folders) {
            FolderAttachmentListDTO folderAttachmentListDTO = new FolderAttachmentListDTO();
            folderAttachmentListDTO.setId(folder.getId());
            folderAttachmentListDTO.setName(folder.getFolderName());

            List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getExamId, id).eq(Attachment::getAttachmentFolderId, folderAttachmentListDTO.getId()));
            getResource(folderAttachmentListDTOS, folderAttachmentListDTO, attachmentList);
        }

        FolderAttachmentListDTO folderAttachmentListDTO = new FolderAttachmentListDTO();
        folderAttachmentListDTO.setId(null);
        folderAttachmentListDTO.setName(null);
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getExamId, id).isNull(Attachment::getAttachmentFolderId));
        if (!attachmentList.isEmpty()) {
            getResource(folderAttachmentListDTOS, folderAttachmentListDTO, attachmentList);
        }

        return ResponseEntity.ok(folderAttachmentListDTOS);
    }

    @GetMapping(value = "/get-ppt")
    public ResponseEntity<List<FolderAttachmentListDTO>> getPpt(@RequestParam String id) {
        List<AttachmentFolder> folders = attachmentFolderService.list(new LambdaQueryWrapper<AttachmentFolder>().eq(AttachmentFolder::getCourseId, id).eq(AttachmentFolder::getType, "ppt"));
        List<FolderAttachmentListDTO> folderAttachmentListDTOS = new ArrayList<>();

        for (AttachmentFolder folder : folders) {
            FolderAttachmentListDTO folderAttachmentListDTO = new FolderAttachmentListDTO();
            folderAttachmentListDTO.setId(folder.getId());
            folderAttachmentListDTO.setName(folder.getFolderName());

            List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getPptId, id).eq(Attachment::getAttachmentFolderId, folderAttachmentListDTO.getId()));
            getResource(folderAttachmentListDTOS, folderAttachmentListDTO, attachmentList);
        }

        FolderAttachmentListDTO folderAttachmentListDTO = new FolderAttachmentListDTO();
        folderAttachmentListDTO.setId(null);
        folderAttachmentListDTO.setName(null);
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getPptId, id).isNull(Attachment::getAttachmentFolderId));
        if (!attachmentList.isEmpty()) {
            getResource(folderAttachmentListDTOS, folderAttachmentListDTO, attachmentList);
        }

        return ResponseEntity.ok(folderAttachmentListDTOS);
    }

    @GetMapping(value = "/get-exercise")
    public ResponseEntity<List<FolderAttachmentListDTO>> getExercise(@RequestParam String id) {
        List<AttachmentFolder> folders = attachmentFolderService.list(new LambdaQueryWrapper<AttachmentFolder>().eq(AttachmentFolder::getCourseId, id).eq(AttachmentFolder::getType, "exercise"));
        List<FolderAttachmentListDTO> folderAttachmentListDTOS = new ArrayList<>();

        for (AttachmentFolder folder : folders) {
            FolderAttachmentListDTO folderAttachmentListDTO = new FolderAttachmentListDTO();
            folderAttachmentListDTO.setId(folder.getId());
            folderAttachmentListDTO.setName(folder.getFolderName());

            List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getExerciseId, id).eq(Attachment::getAttachmentFolderId, folderAttachmentListDTO.getId()));
            getResource(folderAttachmentListDTOS, folderAttachmentListDTO, attachmentList);
        }

        FolderAttachmentListDTO folderAttachmentListDTO = new FolderAttachmentListDTO();
        folderAttachmentListDTO.setId(null);
        folderAttachmentListDTO.setName(null);
        List<Attachment> attachmentList = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getExerciseId, id).isNull(Attachment::getAttachmentFolderId));
        if (!attachmentList.isEmpty()) {
            getResource(folderAttachmentListDTOS, folderAttachmentListDTO, attachmentList);
        }

        return ResponseEntity.ok(folderAttachmentListDTOS);
    }

    private void getResource(List<FolderAttachmentListDTO> folderAttachmentListDTOS, FolderAttachmentListDTO folderAttachmentListDTO, List<Attachment> attachmentList) {
        List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
        for (var attachment : attachmentList) {
            if (folderAttachmentListDTO.name != null) {
                AttachmentFolder attachmentFolder = courseService.getAttachmentFolder(attachment.getAttachmentFolderId());
                AttachmentDTO attachmentDTO = new AttachmentDTO(attachment.getId(), attachment.getUrl(), attachment.getName(), attachment.getExamId(), attachment.getPptId(), attachment.getExerciseId(), attachment.getAllowDownload(), attachment.getAttachmentFolderId(), attachmentFolder.getFolderName(), attachmentFolder.getParentId());
                attachmentDTOList.add(attachmentDTO);
            } else {
                AttachmentDTO attachmentDTO = new AttachmentDTO(attachment.getId(), attachment.getUrl(), attachment.getName(), attachment.getExamId(), attachment.getPptId(), attachment.getExerciseId(), attachment.getAllowDownload(), attachment.getAttachmentFolderId(), null, null);
                attachmentDTOList.add(attachmentDTO);
            }
        }
        folderAttachmentListDTO.setFiles(attachmentDTOList);

        folderAttachmentListDTOS.add(folderAttachmentListDTO);
    }

    @GetMapping(value = "/grade-list")
    public ResponseEntity<GradeList> getGradeList(@RequestParam Integer id) {
        Assignment assignment = assignmentService.getById(id);
        List<Integer> assignmentSubmissionList = courseService.selectSubmission(id);
        List<Grade> assignmentReviewList = new ArrayList<>();
        for (var submissionId : assignmentSubmissionList) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime submittedAt = LocalDateTime.parse(assignmentSubmissionService.getById(submissionId).getSubmittedAt(), formatter);
            LocalDateTime end = LocalDateTime.parse(assignment.getEnd(), formatter);
            Grade grade = new Grade(submissionId, courseService.selectGrade(submissionId), (submittedAt.isBefore(end)) ? assignment.getFullGrade() : assignment.getDelayedGrade());
            assignmentReviewList.add(grade);
        }

        GradeList gradeList = new GradeList(assignmentReviewList);

        return ResponseEntity.ok(gradeList);
    }

    @PostMapping(value = "/create-attachment-folder")
    public ResponseEntity<String> createAttachmentFolder(@RequestBody AttachmentFolder attachmentFolder) {
        if (attachmentFolder.parentId == null) {
            attachmentFolder.parentId = 0;
        }

        courseService.createAttachmentFolder(attachmentFolder);
        Integer id = attachmentFolder.getId();
        return ResponseEntity.ok(String.valueOf(id));
    }

    @DeleteMapping(value = "/delete-attachment-folder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteAttachmentFolder(@RequestParam Integer id) {
        courseService.deleteAttachmentFolder(id);

        return ResponseEntity.noContent().build();
    }

    @Data
    @AllArgsConstructor
    public static class AttachmentResources {

        private Integer id;

        private Integer allowDownload;

        private Integer attachmentFolderId;

    }

    @Data
    @AllArgsConstructor
    public static class CourseResourcesDTO {

        private String id;

        private List<AttachmentResources> attachmentIdList;

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
    public static class AttachmentDTO {

        private Integer id;

        private String url;

        private String name;

        private String examId;

        private String pptId;

        private String exerciseId;

        private Integer allowDownload;

        private Integer attachmentFolderId;

        private String folderName;

        private Integer parentId;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FolderAttachmentListDTO {

        private Integer id;

        private String name;

        private List<AttachmentDTO> files;

    }

    @Data
    @AllArgsConstructor
    public static class AttachmentListDTO {

        private List<AttachmentDTO> attachmentIdList;

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
    public static class Grade {

        private Integer submissionId;

        private Float grade;

        private Float fullGrade;

    }

    @Data
    @AllArgsConstructor
    public static class GradeList {

        private List<Grade> gradeList;

    }

    @Data
    @AllArgsConstructor
    public static class ExportStudentList {
        private String id;
        private String url;
    }

    @Data
    @AllArgsConstructor
    public static class AttachmentFolder {

        private Integer id;

        private String folderName;

        private Integer parentId;

        private String courseId;

        private String type;

    }

}
