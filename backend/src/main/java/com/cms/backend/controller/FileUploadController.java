package com.cms.backend.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.StudentCourseSelectionDTO;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.pojo.DTO.UserDTO;
import com.cms.backend.pojo.DTO.UserTeacherDTO;
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
import java.util.Map;
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

    // 上传学生文件
    @PostMapping("/uploading/users/student")
    public ResponseEntity<String> uploadUserFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), UserDTO.class, new UserUploadListener(userService)).sheet().doRead();
            return ResponseEntity.ok("用户文件上传并处理成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("用户信息导入失败!");
        }
    }

    // 上传教师文件
    @PostMapping("/uploading/users/teacher")
    public ResponseEntity<String> uploadUserTeacherFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), UserTeacherDTO.class, new UserTeacherUploadListener(userService)).sheet().doRead();
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
    @PostMapping("/uploading/course-selection")
    public ResponseEntity<String> uploadCourseSelectionFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), StudentCourseSelectionDTO.class, new CourseSelectionUploadListener(courseService)).sheet().doRead();
            return ResponseEntity.ok("选课文件上传并处理成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("课程信息导入失败！");
        }
    }

    // 上传教课文件
    @PostMapping("/uploading/teach")
    public ResponseEntity<String> uploadTeachingFile(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), TeachingDTO.class, new TeachingUploadListener(courseService)).sheet().doRead();
            return ResponseEntity.ok("授课文件上传并处理成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("授课信息导入失败!");
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
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ReadListener.super.invokeHead(headMap, context);
    }

    @Override
    public void invoke(UserDTO user, AnalysisContext analysisContext) {
        userService.addUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getUserClass(),
                user.getAcademy(),
                user.getGender(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getIdCardNumber()
        );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();

        String time = sdf.format(date);

        userService.addFolder(user.getId(), "默认收藏夹", time, 1, 0);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }

}

@Getter
final class UserTeacherUploadListener implements ReadListener<UserTeacherDTO> {
    private final UserService userService;

    UserTeacherUploadListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ReadListener.super.invokeHead(headMap, context);
    }

    @Override
    public void invoke(UserTeacherDTO user, AnalysisContext analysisContext) {
        userService.addUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getUserClass(),
                user.getAcademy(),
                user.getGender(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getIdCardNumber()
        );
        userService.addUserTeacher(
                user.getId(),
                user.getAddress(),
                user.getTitle(),
                user.getBrief()
        );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        userService.addFolder(user.getId(), "默认收藏夹", time, 1, 0);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }

}

@Getter
final class CourseUploadListener implements ReadListener<Course> {
    private final CourseService courseService;

    CourseUploadListener(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ReadListener.super.invokeHead(headMap, context);
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
        String introduction = course.getIntroduction();

        courseService.addCourse(id, courseNumber, name, semesterYear, classNumber, start, end, academy, teacher, introduction);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }

}

@Getter
final class CourseSelectionUploadListener implements ReadListener<StudentCourseSelectionDTO> {
    private final CourseService courseService;

    CourseSelectionUploadListener(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ReadListener.super.invokeHead(headMap, context);
    }

    @Override
    public void invoke(StudentCourseSelectionDTO studentCourseSelectionDTO, AnalysisContext analysisContext) {
        courseService.addStudentCourseSelection(
                studentCourseSelectionDTO.getStudentId(),
                studentCourseSelectionDTO.getCourseId()
        );
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }

}

@Getter
final class TeachingUploadListener implements ReadListener<TeachingDTO> {
    private final CourseService courseService;

    TeachingUploadListener(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ReadListener.super.invokeHead(headMap, context);
    }

    @Override
    public void invoke(TeachingDTO teachingDTO, AnalysisContext analysisContext) {
        Integer teacherId = teachingDTO.getTeacherId();
        String courseId = teachingDTO.getCourseId();
        courseService.addTeaching(teacherId, courseId);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }

}
