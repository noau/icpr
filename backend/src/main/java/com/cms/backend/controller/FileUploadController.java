package com.cms.backend.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.UserDTO;
import com.cms.backend.service.CourseService;
import com.cms.backend.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RestController
public class FileUploadController {

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

            return ResponseEntity.status(500).body("用户文件处理失败：" + e.getMessage());
        }
    }

    // 上传课程文件
    @PostMapping("/uploading/courses")
    public ResponseEntity<String> uploadCourseFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), Course.class, new CourseUploadListener(courseService)).sheet().doRead();
            return ResponseEntity.ok("课程文件上传并处理成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("课程文件处理失败：" + e.getMessage());
        }
    }
}


@Getter
record UserUploadListener(UserService userService) implements ReadListener<UserDTO> {

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
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}

@Getter
record CourseUploadListener(CourseService courseService) implements ReadListener<Course> {

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

}