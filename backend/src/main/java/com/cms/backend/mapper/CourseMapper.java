package com.cms.backend.mapper;

import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Results({
            @Result(property = "userClass", column = "class"),
            @Result(property = "courseNumber", column = "course_number"),
            @Result(property = "semesterYear", column = "semester_year"),
            @Result(property = "classNumber", column = "class_number")
    })
    @Select("select * from course where id in (select student_course_selection.course_id from student_course_selection where student_id = #{id})")
    List<Course> getAllCourse(Integer id);

    @Results({
            @Result(property = "userClass", column = "class"),
            @Result(property = "defaultFolderId", column = "default_folder_id"),
            @Result(property = "subscriptionsNumber", column = "subscriptions_number"),
            @Result(property = "fansNumber", column = "fans_number")
    })
    @Select("select * from user where id in (select student_course_selection.student_id from student_course_selection where course_id = #{id})")
    List<User> getAllStudents(String id);

    @Results({
            @Result(property = "courseNumber", column = "course_number"),
            @Result(property = "semesterYear", column = "semester_year"),
            @Result(property = "classNumber", column = "class_number")
    })
    @Insert("insert into course values (#{id}, #{courseNumber}, #{name}, #{semesterYear}, #{classNumber}, #{start}, #{end}, #{academy}, #{teacher})")
    void addCourse(String id, String courseNumber, String name, String semesterYear, Integer classNumber, String start, String end, String academy, String teacher);

    @Results({
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "courseId", column = "course_id")
    })
    @Insert("insert into student_course_selection (student_id, course_id) values (#{studentId}, #{courseId})")
    void addStudentCourseSelection(Integer studentId, String courseId);

    @Results({
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "teacherId", column = "teacher_id"),
            @Result(property = "teacherAddress", column = "address")
    })
    @Insert("insert into teaching(course_id,teacher_id) values (#{courseId}, #{teacherId})")
    void addTeaching(Integer teacherId, String courseId);
}
