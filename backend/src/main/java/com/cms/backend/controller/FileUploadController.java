package com.cms.backend.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.StudentCourseSelectionDTO;
import com.cms.backend.pojo.DTO.UserDTO;
import com.cms.backend.service.CourseService;
import com.cms.backend.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@RestController
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final UserService userService;
    private final CourseService courseService;

    public FileUploadController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    // 上传用户文件
    @PostMapping("/uploading/users")
    public ResponseEntity<String> uploadUserFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), UserDTO.class, new UserUploadListener(userService)).sheet().doRead();
            return ResponseEntity.ok("用户文件上传并处理成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("用户信息导入失败!");
        }
    }

    // 上传课程文件
    @PostMapping("/uploading/courses")
    public ResponseEntity<String> uploadCourseFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), Course.class, new CourseUploadListener(courseService)).sheet().doRead();
            return ResponseEntity.ok("课程文件上传并处理成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("课程信息导入失败！");
        }
    }

    // 上传选课文件
    @PostMapping("/uploading/course_selection")
    public ResponseEntity<String> uploadCourseSelectionFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), StudentCourseSelectionDTO.class, new CourseSelectionUploadListener(courseService)).sheet().doRead();
            return ResponseEntity.ok("选课文件上传并处理成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("课程信息导入失败！");
        }
    }

}


@Getter
final class UserUploadListener implements ReadListener<UserDTO> {
    private final UserService userService;

    UserUploadListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(UserDTO user, AnalysisContext analysisContext) {
        userService.addUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getUserClass(),
                user.getAcademy(),
                user.getGender()
        );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();

        String time = sdf.format(date);

        userService.addFolder(user.getId(), "默认收藏夹", time, 1);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserUploadListener) obj;
        return Objects.equals(this.userService, that.userService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService);
    }

    @Override
    public String toString() {
        return "UserUploadListener[" +
                "userService=" + userService + ']';
    }


}

@Getter
final class CourseUploadListener implements ReadListener<Course> {
    private final CourseService courseService;

    CourseUploadListener(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void invoke(Course course, AnalysisContext analysisContext) {
        String id = course.getId();
        String courseNumber = course.getCourseNumber();
        String name = course.getName();
        String semesterYear = course.getSemesterYear();
        Integer classNumber = course.getClassNumber();
        String academy = course.getAcademy();
        String start = course.getStart();
        String end = course.getEnd();
        String teacher = course.getTeacher();
        System.out.println(id);

        courseService.addCourse(id, courseNumber, name, semesterYear, classNumber, start, end, academy, teacher);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CourseUploadListener) obj;
        return Objects.equals(this.courseService, that.courseService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseService);
    }

    @Override
    public String toString() {
        return "CourseUploadListener[" +
                "courseService=" + courseService + ']';
    }


}

@Getter
final class CourseSelectionUploadListener implements ReadListener<StudentCourseSelectionDTO> {
    private final CourseService courseService;

    CourseSelectionUploadListener(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void invoke(StudentCourseSelectionDTO studentCourseSelectionDTO, AnalysisContext analysisContext) {
        System.out.println(studentCourseSelectionDTO.getStudentId());
        courseService.addStudentCourseSelection(
                studentCourseSelectionDTO.getStudentId(),
                studentCourseSelectionDTO.getCourseId()
        );
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CourseSelectionUploadListener) obj;
        return Objects.equals(this.courseService, that.courseService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseService);
    }

    @Override
    public String toString() {
        return "CourseSelectionUploadListener[" +
                "courseService=" + courseService + ']';
    }


}
