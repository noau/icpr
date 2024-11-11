package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cms.backend.pojo.Assignments.Assignment;
import com.cms.backend.pojo.Assignments.AssignmentPeerReview;
import com.cms.backend.pojo.Assignments.AssignmentReview;
import com.cms.backend.pojo.Assignments.AssignmentSubmission;
import com.cms.backend.pojo.Attachment;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.pojo.Notification;
import com.cms.backend.pojo.User;
import com.cms.backend.service.AttachmentService;
import com.cms.backend.service.CourseService;
import com.cms.backend.service.NotificationService;
import com.cms.backend.service.UserService;
import com.cms.backend.service.assignment.AssignmentPeerReviewService;
import com.cms.backend.service.assignment.AssignmentReviewService;
import com.cms.backend.service.assignment.AssignmentService;
import com.cms.backend.service.assignment.AssignmentSubmissionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentSubmissionService assignmentSubmissionService;
    private final AttachmentService attachmentService;
    private final AssignmentPeerReviewService assignmentPeerReviewService;
    private final AssignmentReviewService assignmentReviewService;
    private final NotificationService notificationService;
    private final CourseService courseService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    public AssignmentController(AssignmentService assignmentService, AssignmentSubmissionService assignmentSubmissionService, AttachmentService attachmentService, AssignmentPeerReviewService assignmentPeerReviewService, AssignmentReviewService assignmentReviewService, NotificationService notificationService, CourseService courseService, UserService userService) {
        this.assignmentService = assignmentService;
        this.assignmentSubmissionService = assignmentSubmissionService;
        this.attachmentService = attachmentService;
        this.assignmentPeerReviewService = assignmentPeerReviewService;
        this.assignmentReviewService = assignmentReviewService;
        this.notificationService = notificationService;
        this.courseService = courseService;
        this.userService = userService;
    }

    /**
     * 发布作业
     *
     * @param assignment 作业信息
     * @return 发布作业结果
     */
    @PostMapping("/issue")
    public ResponseEntity<Void> issueAssignment(@RequestBody AssignmentIssue assignment) {
        var newAssignment = new Assignment(0, assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(), assignment.getStart(), assignment.getEnd(), assignment.getIsPrivate(), assignment.getFullGrade(), assignment.getDelayedGrade(), assignment.getLatestEnd(), assignment.getMultipleSubmission(), assignment.getPublishGrade(), assignment.getRequirePeerReview(), assignment.getPeerReviewStart(), assignment.getPeerReviewEnd(), assignment.getMinPeerReview(), assignment.getAnswer());
        assignmentService.save(newAssignment);
        assignment.getAttachments().forEach(attachment ->
                attachmentService.update(
                        new LambdaUpdateWrapper<Attachment>()
                                .eq(Attachment::getId, attachment)
                                .set(Attachment::getAssignmentId, newAssignment.getId())
                )
        );

        List<User> users = courseService.getAllStudents(assignment.courseId);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        TeachingDTO teachingDTO = courseService.getTeacherId(assignment.courseId);
        for (User user : users) {
            Notification notification = new Notification(user.getId(), "新作业来啦~", teachingDTO.getTeacherId(), "作业", assignment.id, assignment.courseId + "作业已发布，请尽快完成", 0, formattedNow, assignment.courseId, 0);
            notificationService.save(notification);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 批改作业
     *
     * @param assignmentReview 作业批改信息
     * @return 批改作业结果
     */
    @PostMapping("/reviews")
    public ResponseEntity<Void> reviewAssignment(@RequestBody AssignmentReview assignmentReview) {
        assignmentReviewService.save(assignmentReview);
        logger.info("Reviewed assignment ID: {}", assignmentReview.getSubmissionId());
        AssignmentSubmission assignmentSubmission = assignmentSubmissionService.getById(assignmentReview.getSubmissionId());
        Assignment assignment = assignmentService.getById(assignmentSubmission.getAssignmentId());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        TeachingDTO teachingDTO = courseService.getTeacherId(assignment.getCourseId());
        Notification notification = new Notification(assignmentSubmission.getStudentId(), "作业已被批改", teachingDTO.getTeacherId(), "作业", assignmentReview.getSubmissionId(), "成绩：" + assignmentReview.getGrade(), 0, formattedNow, assignment.getCourseId(), 0);
        notificationService.save(notification);

        return ResponseEntity.ok().build();
    }

    /**
     * 提交作业
     *
     * @param submission 作业提交信息
     * @return 提交作业结果
     */
    @PostMapping("/submissions")
    public ResponseEntity<Void> submitAssignment(@RequestBody AssignmentSubmissionDTO submission) {
        var newSubmission = new AssignmentSubmission(0, submission.getAssignmentId(), submission.getStudentId(), submission.getSubmittedAt(), submission.getContent());
        assignmentSubmissionService.save(newSubmission);
        submission.getAttachments().forEach(attachment ->
                attachmentService.update(
                        new LambdaUpdateWrapper<Attachment>()
                                .eq(Attachment::getId, attachment)
                                .set(Attachment::getSubmissionId, newSubmission.getId())
                )
        );
        return ResponseEntity.ok().build();
    }

    /**
     * 互评作业
     *
     * @param peerReview 作业互评信息
     * @return 互评作业结果
     */
    @PostMapping("/peer-reviews")
    public ResponseEntity<Void> peerReviewAssignment(@RequestBody AssignmentPeerReview peerReview) {
        assignmentPeerReviewService.save(peerReview);
        logger.info("Peer-reviewed assignment ID: {}", peerReview.getSubmissionId());
        return ResponseEntity.ok().build();
    }

    /**
     * 公布作业答案
     *
     * @return 公布作业答案结果
     */
    @PostMapping("/issue-answer")
    public ResponseEntity<Void> issueAnswer(@RequestBody IssueAnswer answer) {
        assignmentService.update(
                new LambdaUpdateWrapper<Assignment>()
                        .eq(Assignment::getId, answer.assignmentId)
                        .set(Assignment::getAnswer, answer.content)
        );
        answer.attachments.forEach(attachment ->
                attachmentService.update(
                        new LambdaUpdateWrapper<Attachment>()
                                .eq(Attachment::getId, attachment)
                                .set(Attachment::getAnswerId, answer.assignmentId)
                )
        );
        return ResponseEntity.ok().build();
    }

    /**
     * 修改作业
     *
     * @param assignment 作业信息
     * @return 修改作业结果
     */
    @PostMapping("/change")
    public ResponseEntity<Void> changeAssignment(@RequestBody AssignmentIssue assignment) {
        var newAssignment = new Assignment(assignment.getId(), assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(), assignment.getStart(), assignment.getEnd(), assignment.getIsPrivate(), assignment.getFullGrade(), assignment.getDelayedGrade(), assignment.getLatestEnd(), assignment.getMultipleSubmission(), assignment.getPublishGrade(), assignment.getRequirePeerReview(), assignment.getPeerReviewStart(), assignment.getPeerReviewEnd(), assignment.getMinPeerReview(), assignment.getAnswer());
        assignmentService.updateById(newAssignment);
        assignment.getAttachments().forEach(attachment ->
                attachmentService.update(
                        new LambdaUpdateWrapper<Attachment>()
                                .eq(Attachment::getId, attachment)
                                .set(Attachment::getAssignmentId, newAssignment.getId())
                )
        );
        return ResponseEntity.ok().build();
    }

    /**
     * 删除作业
     *
     * @param id 作业ID
     * @return 删除作业结果
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAssignment(@RequestParam Integer id) {
        assignmentService.removeById(id);
        logger.info("Deleted assignment ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获得作业详情
     *
     * @param id 作业ID
     * @return 获得作业详情结果
     */
    @GetMapping("/get-info")
    public ResponseEntity<DescriptionDTO> getAssignmentDescription(@RequestParam Integer id) {
        Assignment assignment = assignmentService.getById(id);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }

        DescriptionDTO descriptionDTO = new DescriptionDTO();
        descriptionDTO.setId(assignment.getId());
        descriptionDTO.setCourseId(assignment.getCourseId());
        descriptionDTO.setTitle(assignment.getTitle());
        descriptionDTO.setDescription(assignment.getDescription());
        descriptionDTO.setStart(assignment.getStart());
        descriptionDTO.setEnd(assignment.getEnd());
        descriptionDTO.setIsPrivate(assignment.getIsPrivate());
        descriptionDTO.setGrade(assignment.getFullGrade().intValue());
        descriptionDTO.setDelayedGrade(assignment.getDelayedGrade().intValue());
        descriptionDTO.setLatestEnd(assignment.getLatestEnd());
        descriptionDTO.setMultipleSubmission(assignment.getMultipleSubmission());
        descriptionDTO.setPublishGrade(assignment.getPublishGrade());
        descriptionDTO.setRequirePeerReview(assignment.getRequirePeerReview());
        descriptionDTO.setPeerReviewStart(assignment.getPeerReviewStart());
        descriptionDTO.setPeerReviewEnd(assignment.getPeerReviewEnd());
        descriptionDTO.setMinPeerReview(assignment.getMinPeerReview());

        // 查询附件
        List<Attachment> attachments = attachmentService.findByAssignmentId(assignment.getId());
        if (attachments != null && !attachments.isEmpty()) {
            List<Integer> attachmentIds = attachments.stream()
                    .map(Attachment::getId)
                    .collect(Collectors.toList());
            descriptionDTO.setAttachments(attachmentIds);
        }

        return ResponseEntity.ok(descriptionDTO);
    }


    /**
     * 课程下的所有應該批改的作业
     *
     * @param id 课程ID
     * @return 获得批改作业列表
     */
    @GetMapping("/course-assignments")
    public ResponseEntity<List<Assignment>> getCourseAssignments(@RequestParam String id) {
        return ResponseEntity.ok(assignmentService.list(new LambdaQueryWrapper<Assignment>().eq(Assignment::getCourseId, id)));
    }

    @GetMapping("/review-list")
    public ResponseEntity<AssignmentReviewListDTO> getAssignmentsSubmissions(@RequestParam Integer id) {

        // 获取与指定作业ID匹配的提交记录
        var submissions = assignmentSubmissionService.list(new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, id));

        var shouldBeSubmitted = courseService.getAllStudents(assignmentService.getById(id).getCourseId()).size();
        var submitted = submissions.size();
        var notSubmitted = shouldBeSubmitted - submitted;

        var details = submissions.stream().map(submission -> {
            // 根据 submission 的 ID 查询相关附件 ID
            var attachments = attachmentService.list(new LambdaQueryWrapper<Attachment>()
                    .eq(Attachment::getSubmissionId, submission.getId()));
            List<AttachmentListDTO> attachmentList = new ArrayList<>();
            for (Attachment attachment : attachments) {
                AttachmentListDTO attachmentDTO = new AttachmentListDTO(attachment.getId(), attachment.getName());
                attachmentList.add(attachmentDTO);
            }

            // 使用 studentId 查询学生姓名
            User student = userService.findById(submission.getStudentId());
            Float grade = assignmentReviewService.findById(submission.getId());

            // 创建 AssignmentSubmissionDetail 对象并设置学生姓名
            AssignmentSubmissionDetailDTO detail = new AssignmentSubmissionDetailDTO(submission, attachmentList);
            detail.setName(student.getName()); // 设置 name 属性
            detail.setGrade(grade);
            return detail;
        }).collect(Collectors.toList());

        AssignmentReviewListDTO assignmentReviewListDTO = new AssignmentReviewListDTO(details, submitted, shouldBeSubmitted, notSubmitted);

        return ResponseEntity.ok(assignmentReviewListDTO);
    }


    /**
     * 作业的所有提交
     *
     * @param assignmentId 作业ID
     * @param userId       评分人的ID
     * @param count        需要评分的作业个数
     * @return 获得作业提交列表
     */
    @GetMapping("/peer-review-list")
    public ResponseEntity<List<AssignmentSubmissionDetail>> getAssignmentsPeerReviewList(@RequestParam Integer assignmentId, @RequestParam Integer userId, @RequestParam Integer count) {
        var submissions = assignmentSubmissionService.list(new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, assignmentId));
        var details = submissions.stream().filter(submission -> !Objects.equals(submission.getStudentId(), userId)).map(submission -> {
            var attachments = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getSubmissionId, submission.getId()).select(Attachment::getId))
                    .stream().map(Attachment::getId).toList();
            return new AssignmentSubmissionDetail(submission, attachments);
        }).collect(Collectors.toList());
        Collections.shuffle(details);
        var review_count = Math.min(count, details.size());
        return ResponseEntity.ok(details.subList(0, review_count));
    }

    @GetMapping("/course-assignments/student")
    public ResponseEntity<List<AssignmentStudent>> getCourseAssignmentsStudent(@RequestBody AssignmentsStudentBody assignmentsStudentBody) {
        String id = assignmentsStudentBody.id;
        Integer userId = assignmentsStudentBody.userId;
        List<Assignment> assignmentList = assignmentService.list(new LambdaQueryWrapper<Assignment>().eq(Assignment::getCourseId, id));
        List<AssignmentStudent> assignmentStudentList = new ArrayList<>();
        for (Assignment assignment : assignmentList) {
            List<AssignmentSubmission> assignmentSubmissionList = assignmentSubmissionService.list(new QueryWrapper<AssignmentSubmission>().eq("assignment_id", assignment.getId()).eq("student_id", userId));
            Float grade = null;
            for (AssignmentSubmission assignmentSubmission : assignmentSubmissionList) {
                AssignmentReview assignmentReview = assignmentReviewService.getOne(new LambdaQueryWrapper<AssignmentReview>().eq(AssignmentReview::getSubmissionId, assignmentSubmission.getId()));
                if (assignmentReview != null) {
                    grade = assignmentReview.getGrade();
                    break;
                }
            }

            var peerReviewCount = assignmentPeerReviewService.getPeerReviewCount(userId, assignment.getId());
            AssignmentStudent assignmentStudent = new AssignmentStudent(assignment.getId(),
                    assignment.getCourseId(),
                    assignment.getTitle(),
                    assignment.getDescription(),
                    assignment.getStart(),
                    assignment.getEnd(),
                    assignment.getIsPrivate(),
                    assignment.getFullGrade(),
                    assignment.getDelayedGrade(),
                    assignment.getLatestEnd(),
                    assignment.getMultipleSubmission(),
                    assignment.getPublishGrade(),
                    assignment.getRequirePeerReview(),
                    assignment.getPeerReviewStart(),
                    assignment.getPeerReviewEnd(),
                    assignment.getMinPeerReview(),
                    assignment.getAnswer(),
                    grade == null ? 1 : 0,
                    grade,
                    peerReviewCount >= assignment.getMinPeerReview()
            );

            assignmentStudentList.add(assignmentStudent);
        }

        return ResponseEntity.ok(assignmentStudentList);
    }

    @Data
    @AllArgsConstructor
    public static class AssignmentsStudentBody {

        private String id;

        private Integer userId;

    }

    @Data
    @AllArgsConstructor
    public static class AssignmentStudent {

        private Integer id;

        private String courseId;

        private String title;

        private String description;

        private String start;

        private String end;

        private Integer isPrivate;

        private Float fullGrade;

        private Float delayedGrade;

        private String latestEnd;

        private Integer multipleSubmission;

        private Integer publishGrade;

        private Integer requirePeerReview;

        private String peerReviewStart;

        private String peerReviewEnd;

        private Integer minPeerReview;

        private String answer;

        private Integer isGrade;

        private Float grade;

        private boolean peerReviewFinished;

    }

    @Data
    public static class IssueAnswer {
        private Integer assignmentId;
        private String content;
        private List<Integer> attachments;
    }

    @Data
    public static class AssignmentSubmissionDTO {
        private Integer assignmentId;
        private Integer studentId;
        private String submittedAt;
        private String content;
        private List<Integer> attachments;
    }

    @Data
    public static class AssignmentIssue {
        private Integer id;
        private String courseId;
        private String title;
        private String description;
        private String start;
        private String end;
        private Integer isPrivate;
        private Float fullGrade;
        private Float delayedGrade;
        private String latestEnd;
        private Integer multipleSubmission;
        private Integer publishGrade;
        private Integer requirePeerReview;
        private String peerReviewStart;
        private String peerReviewEnd;
        private Integer minPeerReview;
        private List<Integer> attachments;
        private String answer;
    }

    @Data
    public static class DescriptionDTO {
        private Integer id;
        private String courseId;
        private String title;
        private String description;
        private String start;
        private String end;
        private Integer isPrivate;
        private Integer grade;
        private Integer delayedGrade;
        private String latestEnd;
        private Integer multipleSubmission;
        private Integer publishGrade;
        private Integer requirePeerReview;
        private String peerReviewStart;
        private String peerReviewEnd;
        private Integer minPeerReview;
        private List<Integer> attachments;
    }

    @Data
    public static class AssignmentSubmissionDetail {
        public AssignmentSubmissionDetail(AssignmentSubmission submission, List<Integer> attachments) {
            this.id = submission.getId();
            this.assignmentId = submission.getAssignmentId();
            this.studentId = submission.getStudentId();
            this.submittedAt = submission.getSubmittedAt();
            this.content = submission.getContent();
            this.attachments = attachments;
        }

        private Integer id;
        private String name;
        private Integer assignmentId;
        private Integer studentId;
        private String submittedAt;
        private String content;
        private List<Integer> attachments;
        private Float grade;

    }

    @Data
    public static class AssignmentSubmissionDetailDTO {
        public AssignmentSubmissionDetailDTO(AssignmentSubmission submission, List<AttachmentListDTO> attachments) {
            this.id = submission.getId();
            this.assignmentId = submission.getAssignmentId();
            this.studentId = submission.getStudentId();
            this.submittedAt = submission.getSubmittedAt();
            this.content = submission.getContent();
            this.attachments = attachments;
        }

        private Integer id;
        private String name;
        private Integer assignmentId;
        private Integer studentId;
        private String submittedAt;
        private String content;
        private List<AttachmentListDTO> attachments;
        private Float grade;

    }

    @Data
    @AllArgsConstructor
    public static class AssignmentReviewListDTO {

        private List<AssignmentSubmissionDetailDTO> submissions;
        private Integer submitted;
        private Integer shouldBeSubmitted;
        private Integer notSubmitted;

    }

    @Data
    @AllArgsConstructor
    public static class AttachmentListDTO {

        private Integer id;

        private String name;

    }

}
