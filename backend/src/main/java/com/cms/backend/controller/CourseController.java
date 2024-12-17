package com.cms.backend.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
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
                .map(user -> new Student(user.getId(), user.getName(), user.getAcademy(), user.getEmail(), user.getPhoneNumber()))
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
        List<FolderAttachmentListDTO> folderAttachmentListDTOS = getFolderAttachmentListDTOS(id,"exam");
        return ResponseEntity.ok(folderAttachmentListDTOS);
    }

    @GetMapping(value = "/get-ppt")
    public ResponseEntity<List<FolderAttachmentListDTO>> getPpt(@RequestParam String id) {
        List<FolderAttachmentListDTO> folderAttachmentListDTOS = getFolderAttachmentListDTOS(id,"ppt");
        return ResponseEntity.ok(folderAttachmentListDTOS);
    }

    @GetMapping(value = "/get-exercise")
    public ResponseEntity<List<FolderAttachmentListDTO>> getExercise(@RequestParam String id) {
        List<FolderAttachmentListDTO> folderAttachmentListDTOS = getFolderAttachmentListDTOS(id,"exercise");
        return ResponseEntity.ok(folderAttachmentListDTOS);
    }

    /**
     * 获取指定课程和类型下的文件夹及其包含的文件列表。
     *
     * @param id   课程ID或相关标识，用于查询文件夹和文件
     * @param type 文件夹类型，用于查询指定类型的文件夹
     * @return 返回一个包含文件夹及文件信息的列表
     */
    private List<FolderAttachmentListDTO> getFolderAttachmentListDTOS(String id, String type) {
        // 根据课程ID和文件夹类型查询所有符合条件的文件夹
        List<AttachmentFolder> folders = attachmentFolderService.list(new LambdaQueryWrapper<AttachmentFolder>()
                .eq(AttachmentFolder::getCourseId, id) // 通过课程ID过滤文件夹
                .eq(AttachmentFolder::getType, type)   // 通过文件夹类型过滤文件夹
        );

        // 存储文件夹和文件信息的DTO列表
        List<FolderAttachmentListDTO> folderAttachmentListDTOS = new ArrayList<>();

        // 遍历每个文件夹
        for (AttachmentFolder folder : folders) {
            // 创建一个FolderAttachmentListDTO对象，保存文件夹的相关信息
            FolderAttachmentListDTO folderAttachmentListDTO = new FolderAttachmentListDTO();

            // 用于存储文件夹内的所有文件DTO列表
            List<AttachmentDTO> attachmentDTOList = new ArrayList<>();

            // 设置文件夹的ID和名称
            folderAttachmentListDTO.setId(folder.getId());
            folderAttachmentListDTO.setName(folder.getFolderName());

            // 根据文件夹ID和课程ID查询该文件夹下的所有文件
            LambdaQueryWrapper<Attachment> wrapper = new LambdaQueryWrapper<Attachment>().eq(Attachment::getAttachmentFolderId, folderAttachmentListDTO.getId());
            if (type.equals("ppt")) {
                wrapper.eq(Attachment::getPptId,id);
            } else if (type.equals("exam")) {
                wrapper.eq(Attachment::getExamId,id);
            } else{
                wrapper.eq(Attachment::getExerciseId,id);
            }
            List<Attachment> attachmentList = attachmentService.list(wrapper);

            // 如果文件夹内有文件
            if (!attachmentList.isEmpty()) {
                // 遍历文件夹中的每个文件
                for (Attachment attachment : attachmentList) {
                    // 创建文件DTO对象，将文件属性复制到DTO
                    AttachmentDTO attachmentDTO = new AttachmentDTO();
                    BeanUtils.copyProperties(attachment, attachmentDTO); // 属性复制

                    // 设置文件的父文件夹ID和文件夹名称
                    attachmentDTO.setParentId(folderAttachmentListDTO.getId());
                    attachmentDTO.setFolderName(folder.getFolderName());

                    // 将文件DTO添加到文件列表中
                    attachmentDTOList.add(attachmentDTO);
                }
                // 设置文件夹内的文件列表
                folderAttachmentListDTO.setFiles(attachmentDTOList);
            }

            // 将文件夹和文件信息DTO添加到最终的结果列表中
            folderAttachmentListDTOS.add(folderAttachmentListDTO);
        }

        // 返回包含所有文件夹及其文件信息的列表
        return folderAttachmentListDTOS;
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

    /**
     * 编辑文件夹
     * @param dto
     * @return
     */
    @PostMapping(value = "/edit-attachment-folder")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> editAttachmentFolder(@RequestBody EditAttachmentFolder dto) {
        courseService.editAttachmentFolder(dto.id,dto.folderName);

        return ResponseEntity.ok("修改成功");
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
        @ExcelProperty("学号")
        private Integer id;

        @ExcelProperty("姓名")
        private String name;

        @ExcelProperty("学院")
        private String academy;

        @ExcelProperty("电子邮件")
        private String email;

        @ExcelProperty("手机号")
        private String phoneNumber;
    }

    @Data
    @AllArgsConstructor
    public static class CourseList {

        private List<Course> courses;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
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
    @NoArgsConstructor
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
    @Data
    @AllArgsConstructor
    public static class EditAttachmentFolder {

        private Integer id;

        private String folderName;

    }
}
