package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cms.backend.pojo.Assignments.*;
import com.cms.backend.pojo.Attachment;
import com.cms.backend.pojo.DTO.Assignment.DescriptionDTO;
import com.cms.backend.service.assignment.AssignmentReviewService;
import com.cms.backend.service.assignment.AssignmentService;
import com.cms.backend.service.assignment.AssignmentSubmissionService;
import com.cms.backend.service.assignment.AssignmentPeerReviewService;
import com.cms.backend.service.AttachmentService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    private final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    public AssignmentController(AssignmentService assignmentService, AssignmentSubmissionService assignmentSubmissionService, AttachmentService attachmentService, AssignmentPeerReviewService assignmentPeerReviewService, AssignmentReviewService assignmentReviewService) {
        this.assignmentService = assignmentService;
        this.assignmentSubmissionService = assignmentSubmissionService;
        this.attachmentService = attachmentService;
        this.assignmentPeerReviewService = assignmentPeerReviewService;
        this.assignmentReviewService = assignmentReviewService;
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
        System.out.println(newAssignment.getId());
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
     * 批改作业
     *
     * @param assignmentReview 作业批改信息
     * @return 批改作业结果
     */
    @PostMapping("/reviews")
    public ResponseEntity<Void> reviewAssignment(@RequestBody AssignmentReview assignmentReview) {
        assignmentReviewService.save(assignmentReview);
        logger.info("Reviewed assignment ID: {}", assignmentReview.getSubmissionId());
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
        String description = assignmentService.getAssignmentDescription(id);
        return ResponseEntity.ok(new DescriptionDTO(description));
    }

    /**
     * 课程下的所有作业
     *
     * @param id 课程ID
     * @return 获得作业列表
     */
    @GetMapping("/course-assignments")
    public ResponseEntity<List<Assignment>> getCourseAssignments(@RequestParam String id) {
        return ResponseEntity.ok(assignmentService.list(new LambdaQueryWrapper<Assignment>().eq(Assignment::getCourseId, id)));
    }

    /**
     * 作业的所有提交
     *
     * @param id 作业ID
     * @return 获得作业提交列表
     */
    @GetMapping("/review-list")
    public ResponseEntity<List<AssignmentSubmissionDetail>> getAssignmentsSubmissions(@RequestParam Integer id) {
        var submissions = assignmentSubmissionService.list(new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, id));
        var details = submissions.stream().map(submission -> {
           var attachments = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getSubmissionId, submission.getId()).select(Attachment::getId))
                   .stream().map(Attachment::getId).toList();
           return new AssignmentSubmissionDetail(submission, attachments);
        });
        return ResponseEntity.ok(details.collect(Collectors.toList()));
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

        private Integer assignmentId;

        private Integer studentId;

        private String submittedAt;

        private String content;

        private List<Integer> attachments;
    }
}
