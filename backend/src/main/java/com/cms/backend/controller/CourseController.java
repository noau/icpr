package com.cms.backend.controller;

import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.AllStudentsDTO;
import com.cms.backend.pojo.DTO.CourseDTO;
import com.cms.backend.pojo.User;
import com.cms.backend.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/all")
    public ResponseEntity<CourseDTO> getAllCourse(@RequestParam Integer id) {
        List<Course> courses = courseService.getAllCourse(id);
        CourseDTO courseDTO = new CourseDTO(courses);
        return ResponseEntity.ok(courseDTO);
    }

    @GetMapping(value = "/all-students")
    public ResponseEntity<AllStudentsDTO> getAllStudents(@RequestParam String id) {
        System.out.println(id);
        List<User> users = courseService.getAllStudents(id);
        AllStudentsDTO allStudentsDTO = new AllStudentsDTO(users);
        return ResponseEntity.ok(allStudentsDTO);
    }

}
