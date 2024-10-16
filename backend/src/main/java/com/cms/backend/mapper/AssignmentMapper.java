package com.cms.backend.mapper;

import com.cms.backend.pojo.Assignments.Assignment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AssignmentMapper {

    /**
     * 发布作业
     *
     * @param courseId           课程ID
     * @param title              作业标题
     * @param description        作业描述
     * @param start              作业开始时间
     * @param end                作业结束时间
     * @param isPrivate          是否私有（0或1）
     * @param fullGrade          满分
     * @param delayedGrade       延迟提交的分数
     * @param latestEnd          最晚提交时间
     * @param multipleSubmission 是否允许多次提交（0或1）
     * @param publishGrade       是否公布成绩（0或1）
     * @param requirePeerReview  是否需要互评（0或1）
     * @param peerReviewStart    互评开始时间
     * @param peerReviewEnd      互评结束时间
     * @param minPeerReview      最少互评人数
     */
    @Results({
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "isPrivate", column = "is_private"),
            @Result(property = "fullGrade", column = "full_grade"),
            @Result(property = "delayedGrade", column = "delayed_grade"),
            @Result(property = "latestEnd", column = "latest_end"),
            @Result(property = "multipleSubmission", column = "multiple_submission"),
            @Result(property = "publishGrade", column = "publish_grade"),
            @Result(property = "requirePeerReview", column = "require_peer_review"),
            @Result(property = "peerReviewStart", column = "peer_review_start"),
            @Result(property = "peerReviewEnd", column = "peer_reviewEnd"),
            @Result(property = "minPeerReview", column = "min_peer_review")
    })
    @Insert("insert into assignment(course_id, title, description, end, start, is_private, full_grade, delayed_grade, latest_end, multiple_submission, publish_grade, require_peer_review, peer_review_start, peer_review_end, min_peer_review)" +
            "values (#{courseId}, #{title}, #{description}, #{end}, #{start}, #{isPrivate}, #{fullGrade}, #{delayedGrade}, #{latestEnd}, #{multipleSubmission}, #{publishGrade}, #{requirePeerReview}, #{peerReviewStart}, #{peerReviewEnd}, #{minPeerReview})")
    Integer addAssignment(String courseId, String title, String description,
                          String start, String end, Integer isPrivate, Float fullGrade,
                          Float delayedGrade, String latestEnd, Integer multipleSubmission,
                          Integer publishGrade, Integer requirePeerReview, String peerReviewStart, String peerReviewEnd, Integer minPeerReview);

    /**
     * 批改作业
     *
     * @param submissionId 提交ID
     * @param grade        成绩
     * @param feedback     反馈
     * @param gradeAt      评分时间
     */
    @Results({
            @Result(property = "submissionId", column = "submission_id"),
            @Result(property = "gradeAt", column = "grade_at")
    })
    @Insert("insert into assignment_review values (#{submissionId}, #{grade}, #{feedback}, #{gradeAt})")
    void reviewAssignment(Integer submissionId, Float grade, String feedback, String gradeAt);

    /**
     * 提交作业
     *
     * @param assignmentId 作业ID
     * @param studentId    学生ID
     * @param submittedAt  提交时间
     * @param content      作业内容
     */
    @Results({
            @Result(property = "assignmentId", column = "assignment_id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "submittedAt", column = "submitted_at")
    })
    @Insert("insert into assignment_submission values (#{assignmentId}, #{studentId}, #{submittedAt},#{content})")
    void submissionAssignment(Integer assignmentId, Integer studentId, String submittedAt, String content);

    /**
     * 互评作业
     *
     * @param reviewerId 评审者ID
     * @param grade      成绩
     * @param feedback   反馈
     * @param reviewedAt 评审时间
     */
    @Results({
            @Result(property = "reviewerId", column = "reviewer_id"),
            @Result(property = "reviewedAt", column = "reviewer_at")
    })
    @Insert("insert into assignment_peer_review values (#{reviewerId}, #{grade}, #{feedback},#{reviewedAt})")
    void peerReviewsAssignment(Integer reviewerId, Float grade, String feedback, String reviewedAt);

    /**
     * 公布作业答案
     *
     * @param assignmentId 作业ID
     * @param content      答案内容
     */

    void issueAnswer(Integer assignmentId, String content);

    /**
     * 更改作业
     *
     * @param id                 作业ID
     * @param courseId           课程ID
     * @param title              作业标题
     * @param description        作业描述
     * @param start              作业开始时间
     * @param end                作业结束时间
     * @param isPrivate          是否私有（0或1）
     * @param fullGrade          满分
     * @param delayedGrade       延迟提交的分数
     * @param latestEnd          最晚提交时间
     * @param multipleSubmission 是否允许多次提交（0或1）
     * @param publishGrade       是否公布成绩（0或1）
     * @param requirePeerReview  是否需要互评（0或1）
     * @param peerReviewStart    互评开始时间
     * @param peerReviewEnd      互评结束时间
     * @param minPeerReview      最少互评人数
     */
    @Results({
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "isPrivate", column = "is_private"),
            @Result(property = "fullGrade", column = "full_grade"),
            @Result(property = "delayedGrade", column = "delayed_grade"),
            @Result(property = "latestEnd", column = "latest_end"),
            @Result(property = "multipleSubmission", column = "multiple_submission"),
            @Result(property = "publishGrade", column = "publish_grade"),
            @Result(property = "requirePeerReview", column = "require_peer_review"),
            @Result(property = "peerReviewStart", column = "peer_review_start"),
            @Result(property = "peerReviewEnd", column = "peer_reviewEnd"),
            @Result(property = "minPeerReview", column = "min_peer_review")
    })
    @Insert("update assignment set course_id = #{courseId},title =  #{title},description = #{description},start = #{start},end = #{end},is_private = #{isPrivate},full_grade = #{fullGrade},delayed_grade = #{delayedGrade},latest_end = #{latestEnd},multiple_submission = #{multipleSubmission},publish_grade = #{publishGrade},require_peer_review = #{requirePeerReview},peer_review_start =#{peerReviewStart}, peer_review_end = #{peerReviewEnd},min_peer_review = #{minPeerReview} where id = #{id}")
    void changeAssignment(Integer id, String courseId, String title, String description, String start, String end, Integer isPrivate, Float fullGrade, Float delayedGrade, String latestEnd, Integer multipleSubmission, Integer publishGrade, Integer requirePeerReview, String peerReviewStart, String peerReviewEnd, Integer minPeerReview);

    /**
     * 删除作业
     *
     * @param id Integer 作业ID
     */
    @Delete("delete from assignment where id = #{id}")
    void deleteAssignment(Integer id);

    /**
     * 获取作业详情
     *
     * @param id Integer 作业ID
     * @return 作业描述
     */
    @Select("select * from assignment where id = #{id}")
    String getAssignmentDescription(Integer id);

    /**
     * 发布作业附件
     *
     * @param attachmentId 附件ID
     * @param assignmentId 作业ID
     */
    @Update("update attachment set assignment_id = #{assignmentId} where id = #{attachmentId}")
    void findAssignmentAttachments(Integer attachmentId, Integer assignmentId);

    /**
     * 提交作业附件
     *
     * @param attachmentId 附件ID
     * @param submissionId 提交ID
     */
    @Update("update attachment set submission_id = #{submissionId} where id = #{attachmentId}")
    void findSubmissionAttachments(Integer attachmentId, Integer submissionId);

    /**
     * 提交作业答案附件
     *
     * @param attachmentId 附件ID
     * @param assignmentId 作业ID
     */
    @Update("update attachment set answer_id = #{assignmentId} where id = #{attachmentId}")
    void findAnswerAttachments(Integer attachmentId, Integer assignmentId);
}
