package com.cms.backend.controller;

import com.cms.backend.pojo.Assignments.*;
import com.cms.backend.service.AssignmentService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    private final Logger log = LoggerFactory.getLogger(AssignmentController.class);

    /**
     * 发布作业
     *
     * @param assignment 作业信息
     * @return 发布作业结果
     */
    @PostMapping("/issue")
    public ResponseEntity<Void> issueAssignment(@RequestBody AssignmentIssue assignment) {

        var newAssignment = new Assignment(0,assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(), assignment.getStart(), assignment.getEnd(), assignment.getIsPrivate(), assignment.getFullGrade(), assignment.getDelayedGrade(), assignment.getLatestEnd(), assignment.getMultipleSubmission(), assignment.getPublishGrade(), assignment.getRequirePeerReview(), assignment.getPeerReviewStart(), assignment.getPeerReviewEnd(), assignment.getMinPeerReview());
        assignmentService.save(newAssignment);

        System.out.println(newAssignment.getId());

//        assignment.getAttachments().forEach(attachmentId -> assignmentService.findAssignmentAttachments(attachmentId, newAssignment.getId()));

        return ResponseEntity.ok().build();
    }
//
//    /**
//     * 批改作业
//     *
//     * @param assignmentReview 作业批改信息
//     * @return 批改作业结果
//     */
//    @PostMapping("/reviews")
//    public ResponseEntity<Void> reviewAssignment(@RequestBody AssignmentReview assignmentReview) {
//        assignmentService.reviewsAssignment(assignmentReview.getSubmissionId(), assignmentReview.getGrade(), assignmentReview.getFeedback(), assignmentReview.getGradedAt());
//        log.info("Reviewed assignment ID: {}", assignmentReview.getSubmissionId());
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 提交作业
//     *
//     * @param submission 作业提交信息
//     * @return 提交作业结果
//     */
//    @PostMapping("/submissions")
//    public ResponseEntity<Void> submitAssignment(@RequestBody AssignmentSubmissionDTO submission) {
//        assignmentService.submissionAssignment(submission.getAssignmentId(), submission.getStudentId(), submission.getSubmittedAt(), submission.getContent());
//        submission.getAttachments().forEach(attachmentId -> assignmentService.findSubmissionAttachments(attachmentId, submission.getAssignmentId()));
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 互评作业
//     *
//     * @param peerReview 作业互评信息
//     * @return 互评作业结果
//     */
//    @PostMapping("/peer-reviews")
//    public ResponseEntity<Void> peerReviewAssignment(@RequestBody AssignmentsPeerReview peerReview) {
//        assignmentService.peerReviewsAssignment(peerReview.getReviewerId(), peerReview.getGrade(), peerReview.getFeedback(), peerReview.getReviewedAt());
//        log.info("Peer-reviewed assignment ID: {}", peerReview.getSubmissionId());
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 公布作业答案
//     *
//     * @param submission 作业提交信息
//     * @return 公布作业答案结果
//     */
//    @PostMapping("/issue-answer")
//    public ResponseEntity<Void> issueAnswer(@RequestBody AssignmentSubmissionDTO submission) {
//        assignmentService.issueAnswer(submission.getAssignmentId(), submission.getContent());
//        submission.getAttachments().forEach(attachmentId -> assignmentService.findAnswerAttachments(attachmentId, submission.getAssignmentId()));
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 修改作业
//     *
//     * @param assignment 作业信息
//     * @return 修改作业结果
//     */
//    @PostMapping("/change")
//    public ResponseEntity<Void> changeAssignment(@RequestBody AssignmentIssue assignment) {
//        assignmentService.changeAssignment(assignment.getId(), assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(), assignment.getStart(), assignment.getEnd(), assignment.getIsPrivate(), assignment.getFullGrade(), assignment.getDelayedGrade(), assignment.getLatestEnd(), assignment.getMultipleSubmission(), assignment.getPublishGrade(), assignment.getRequirePeerReview(), assignment.getPeerReviewStart(), assignment.getPeerReviewEnd(), assignment.getMinPeerReview());
//        assignment.getAttachments().forEach(attachmentId -> assignmentService.findSubmissionAttachments(attachmentId, assignment.getId()));
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 删除作业
//     *
//     * @param id 作业ID
//     * @return 删除作业结果
//     */
//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> deleteAssignment(@RequestParam Integer id) {
//        assignmentService.deleteAssignment(id);
//        log.info("Deleted assignment ID: {}", id);
//        return ResponseEntity.noContent().build();
//    }
//
//    /**
//     * 获得作业详情
//     *
//     * @param id 作业ID
//     * @return 获得作业详情结果
//     */
//    @GetMapping("/get-info")
//    public ResponseEntity<DescriptionDTO> getAssignmentDescription(@RequestParam Integer id) {
//        String description = assignmentService.getAssignmentDescription(id);
//        return ResponseEntity.ok(new DescriptionDTO(description));
//    }
}

@Getter
@Setter
class AssignmentIssue {
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
}

@Getter
@Setter
class AssignmentSubmissionDTO {
    private Integer assignmentId;
    private Integer studentId;
    private String submittedAt;
    private String content;
    private List<Integer> attachments;
}
