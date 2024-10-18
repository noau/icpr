package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Course;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.pojo.TeacherInfo;
import com.cms.backend.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

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

    @Results({
            @Result(property = "id", column = "exam_id"),
            @Result(property = "attachmentId", column = "id")
    })
    @Update("update attachment set exam_id = #{courseId} where id = #{attachmentId}")
    void uploadResourceExam(String courseId, Integer attachmentId);

    @Results({
            @Result(property = "id", column = "ppt_id"),
            @Result(property = "attachmentId", column = "id")
    })
    @Update("update attachment set ppt_id = #{courseId} where id = #{attachmentId}")
    void uploadResourcePpt(String courseId, Integer attachmentId);

    @Results({
            @Result(property = "id", column = "exercise_id"),
            @Result(property = "attachmentId", column = "id")
    })
    @Update("update attachment set exercise_id = #{courseId} where id = #{attachmentId}")
    void uploadResourceExercise(String courseId, Integer attachmentId);

    @Results({
            @Result(property = "id", column = "course_id"),
            @Result(property = "teacherId", column = "teacher_id")
    })
    @Select("select teacher_id from teaching where course_id = #{id}")
    TeachingDTO getTeacherId(String id);

    @Select("select id from assignment_submission where assignment_id = #{id}")
    List<Integer> selectSubmission(Integer id);

    @Select("select assignment_review.grade from assignment_review where submission_id = #{submissionId}")
    Float selectGrade(Integer submissionId);

    @Results({
            @Result(property = "id", column = "syllabus_id"),
            @Result(property = "attachmentId", column = "id")
    })
    @Update("update attachment set syllabus_id = #{courseId} where id = #{attachmentId}")
    void uploadResourceSyllabus(String courseId, Integer attachmentId);

    @Results({
            @Result(property = "id", column = "calendar_id"),
            @Result(property = "attachmentId", column = "id")
    })
    @Update("update attachment set calendar_id = #{courseId} where id = #{attachmentId}")
    void uploadResourceCalendar(String courseId, Integer attachmentId);

    @Results({
            @Result(property = "teachingId", column = "teaching_id")
    })
    @Select("select * from teacher_info where teacher_id = #{teachingId}")
    TeacherInfo getTeacherInfo(Integer teachingId);

}
