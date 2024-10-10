package com.cms.backend.mapper;

import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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
}
