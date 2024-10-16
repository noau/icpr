package com.cms.backend.service.impl;


import com.cms.backend.mapper.AssignmentMapper;
import com.cms.backend.service.AssignmentService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentMapper assignmentMapper;

    public AssignmentServiceImpl(AssignmentMapper assignmentMapper) {
        this.assignmentMapper = assignmentMapper;
    }

    //发布作业
    @Override
    public Integer addAssignment(String courseId, String title, String description,
                              String start, String end , Integer isPrivate, Float fullGrade,
                              Float delayedGrade, String latestEnd, Integer multipleSubmission,
                              Integer publishGrade, Integer requirePeerReview, String peerReviewStart, String peerReviewEnd, Integer minPeerReview) {
        return assignmentMapper.addAssignment(courseId,title,description,start,end,isPrivate,fullGrade,delayedGrade,latestEnd,multipleSubmission,publishGrade,requirePeerReview,peerReviewStart,peerReviewEnd,minPeerReview);
    }

    @Override
    public void deleteAssignment(Integer id) {

    }

    //用于批改作业
    @Override
    public void reviewsAssignment(Integer submissionId, Float grade, String feedback, String gradeAt) {
        assignmentMapper.reviewAssignment(submissionId,grade,feedback,gradeAt);
    }

    //用于提交作业
    @Override
    public void submissionAssignment(Integer assignmentId, Integer studentId, String submittedAt, String content) {
        assignmentMapper.submissionAssignment(assignmentId, studentId, submittedAt, content);
    }

    //用于互评作业
    @Override
    public void peerReviewsAssignment(Integer reviewerId, Float grade, String feedback, String reviewedAt) {
        assignmentMapper.peerReviewsAssignment(reviewerId, grade, feedback, reviewedAt);
    }

    //用于公布作业答案
    @Override
    public void issueAnswer(Integer assignmentId, String content) {
        assignmentMapper.issueAnswer(assignmentId,content);
    }

    @Override
    public void changeAssignment(Integer id,String courseId, String title, String description, String start, String end, Integer isPrivate, Float fullGrade, Float delayedGrade, String latestEnd, Integer multipleSubmission, Integer publishGrade, Integer requirePeerReview, String peerReviewStart, String peerReviewEnd, Integer minPeerReview) {
        assignmentMapper.changeAssignment(id ,courseId,title,description,start,end,isPrivate,fullGrade,delayedGrade,latestEnd,multipleSubmission,publishGrade,requirePeerReview,peerReviewStart,peerReviewEnd,minPeerReview);
    }

    @Override
    public void deleteAddress(Integer id) {
        assignmentMapper.deleteAssignment(id);
    }

    @Override
    public String getAssignmentDescription(Integer id) {
        return assignmentMapper.getAssignmentDescription(id);
    }

    @Override
    public void findAssignmentAttachments(Integer attachmentId,Integer assignmentId) {
        assignmentMapper.findAssignmentAttachments(attachmentId,assignmentId);
    }

    @Override
    public void findSubmissionAttachments(Integer attachmentId, Integer submissionId) {
        assignmentMapper.findSubmissionAttachments(attachmentId,submissionId);
    }

    @Override
    public void findAnswerAttachments(Integer attachmentId, Integer assignmentId) {
        assignmentMapper.findAnswerAttachments(attachmentId,assignmentId);
    }

}
