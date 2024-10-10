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
            @Result(property = "classNumber", column = "class_number"),
    })
    @Insert("insert into course values (#{id},#{courseNumber} ,#{name}, #{semesterYear}, #{classNumber}, #{start}, #{end}, #{academy}, #{teacher})")
    void addCourse(String id, String courseNumber, String name, String semesterYear, Integer classNumber, String start, String end, String academy, String teacher);
}
