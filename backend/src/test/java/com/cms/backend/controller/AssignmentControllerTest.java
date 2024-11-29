package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.cms.backend.pojo.Assignments.Assignment;
import com.cms.backend.pojo.Assignments.AssignmentPeerReview;
import com.cms.backend.pojo.Assignments.AssignmentReview;
import com.cms.backend.pojo.Assignments.AssignmentSubmission;
import com.cms.backend.pojo.Attachment;
import com.cms.backend.pojo.DTO.TeachingDTO;
import com.cms.backend.pojo.Notification;
import com.cms.backend.pojo.User;
import org.apache.ibatis.builder.MapperBuilderAssistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import com.cms.backend.service.*;
import com.cms.backend.service.assignment.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
class AssignmentControllerTest {

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private CourseService courseService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AssignmentReviewService assignmentReviewService;

    @Mock
    private AssignmentSubmissionService assignmentSubmissionService;

    @Mock
    private AssignmentPeerReviewService assignmentPeerReviewService;

    @Mock
    private UserService userService;

    @Mock
    private ThreadPoolTaskScheduler taskScheduler;
    @InjectMocks
    private AssignmentController assignmentController;

    private Assignment assignment;
    private AssignmentController.AssignmentIssue  assignmentIssue;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 模拟 taskScheduler
        when(taskScheduler.schedule(any(Runnable.class), any(Instant.class)))
                .thenAnswer(invocation -> {
                    Runnable task = invocation.getArgument(0);
                    task.run();
                    return null; // 返回 null 或 mock 的 ScheduledFuture 对象
                });
        // 将模拟的 taskScheduler 注入到 controller
        ReflectionTestUtils.setField(assignmentController, "taskScheduler", taskScheduler);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Assignment.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Attachment.class);

        assignmentIssue = new AssignmentController.AssignmentIssue();
        assignmentIssue.setCourseId("M310006B1机械学习2024~2025上");
        assignmentIssue.setTitle("测试作业");
        assignmentIssue.setEnd("2024-12-01 23:59:59");
        assignmentIssue.setLatestEnd("2024-12-05 23:59:59");
        assignmentIssue.setPeerReviewStart("2024-12-06 00:00:00");
        assignmentIssue.setPeerReviewEnd("2024-12-10 23:59:59");
        assignmentIssue.setAttachments(List.of(66, 47));
        assignmentIssue.setFullGrade(100F);
        assignmentIssue.setDelayedGrade(90F);
        // 初始化 全局的 Assignment 对象
        assignment = new Assignment(0, assignmentIssue.getCourseId(), assignmentIssue.getTitle(), assignmentIssue.getDescription(), assignmentIssue.getStart(),assignmentIssue.getEnd(), assignmentIssue.getIsPrivate(), assignmentIssue.getFullGrade(), assignmentIssue.getDelayedGrade(), assignmentIssue.getLatestEnd(), assignmentIssue.getMultipleSubmission(), assignmentIssue.getPublishGrade(), assignmentIssue.getRequirePeerReview(), assignmentIssue.getPeerReviewStart(), assignmentIssue.getPeerReviewEnd(), assignmentIssue.getMinPeerReview(), assignmentIssue.getAnswer());

        when(assignmentService.getById(0)).thenReturn(assignment);
        when(assignmentService.update(any())).thenReturn(true);
        when(assignmentService.list(any(LambdaQueryWrapper.class))).thenReturn(List.of(assignment));
        when(courseService.getAllStudents("M310006B1机械学习2024~2025上")).thenReturn(List.of(new User()));
        when(courseService.getTeacherId("M310006B1机械学习2024~2025上")).thenReturn(new TeachingDTO());


        // 初始化 Attachment 对象
        Attachment attachment1 = new Attachment();
        attachment1.setAssignmentId(assignment.getId());
        attachment1.setId(66);
        attachment1.setName("BJTU-ERIQI.pdf");
        attachment1.setUrl("http://localhost:65/BJTU-ERIQI.pdf");
        Attachment attachment2 = new Attachment();
        attachment2.setAssignmentId(assignment.getId());
        attachment2.setId(47);
        attachment2.setName("STEM.pdf");
        attachment2.setUrl("http://localhost:65/STEM.pdf");


        when(attachmentService.getById(66)).thenReturn(attachment1);
        when(attachmentService.getById(47)).thenReturn(attachment2);
        when(attachmentService.updateById(attachment1)).thenReturn(true);
        when(attachmentService.updateById(attachment2)).thenReturn(true);

    }

    @Test
    void issueAssignment() {
        // 调用 Controller 方法
        ResponseEntity<String> response = assignmentController.issueAssignment(assignmentIssue);

        // 验证返回值
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("作业发布成功，已通知所有学生。", response.getBody());

        // 验证服务方法被调用
        verify(assignmentService, times(1)).save(assignment);
        verify(notificationService, atLeastOnce()).save(any(Notification.class));
    }

    @Test
    void reviewAssignment() {
        AssignmentReview assignmentReview = new AssignmentReview(1, 85F,"还不错继续努力","2024-12-11 20:30:20");

        ResponseEntity<Void> response = assignmentController.reviewAssignment(assignmentReview);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(assignmentReviewService, times(1)).saveOrUpdate(assignmentReview);
    }

    @Test
    void submitAssignment() {
        AssignmentController.AssignmentSubmissionDTO submissionDTO = new AssignmentController.AssignmentSubmissionDTO();
        submissionDTO.setAssignmentId(assignment.getId());
        submissionDTO.setStudentId(22123456);
        submissionDTO.setSubmittedAt(LocalDateTime.now().toString());
        submissionDTO.setAttachments(List.of(1,2,3));
        //测试提交逻辑在服务层没有现有记录的情况下是否能够正常保存数据
        when(assignmentSubmissionService.getOne(any())).thenReturn(null);
        //模拟提交作业
        ResponseEntity<Void> response = assignmentController.submitAssignment(submissionDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(assignmentSubmissionService, times(1)).save(any(AssignmentSubmission.class));
    }

    @Test
    void peerReviewAssignment() {
        AssignmentPeerReview peerReview = new AssignmentPeerReview();
        peerReview.setSubmissionId(1);
        peerReview.setGrade(90F);
        peerReview.setReviewedAt(LocalDateTime.now().toString());
        peerReview.setFeedback("还不错");

        ResponseEntity<Void> response = assignmentController.peerReviewAssignment(peerReview);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(assignmentPeerReviewService, times(1)).save(peerReview);
    }

    @Test
    void issueAnswer() {
        AssignmentController.IssueAnswer answer = new AssignmentController.IssueAnswer();
        answer.setAssignmentId(0);
        answer.setContent("我是作业的答案");
        answer.setAttachments(List.of(1, 2, 3));

        when(assignmentService.getById(assignment.getId())).thenReturn(assignment);
        when(courseService.getAllStudents(anyString())).thenReturn(List.of(new User()));
        when(courseService.getTeacherId(anyString())).thenReturn(new TeachingDTO());

        ResponseEntity<Void> response = assignmentController.issueAnswer(answer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(assignmentService, times(1)).update(any());
        verify(notificationService, atLeastOnce()).save(any(Notification.class));
    }

    @Test
    void changeAssignment() {
        assignmentIssue.setTitle("更改作业测试");

        when(courseService.getAllStudents(anyString())).thenReturn(List.of(new User()));
        when(courseService.getTeacherId(anyString())).thenReturn(new TeachingDTO());

        ResponseEntity<Void> response = assignmentController.changeAssignment(assignmentIssue);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(assignmentService, times(1)).updateById(any(Assignment.class));
    }

    @Test
    void deleteAssignment() {
        //获取到全局 assignment 的 Id
        ResponseEntity<Void> response = assignmentController.deleteAssignment(assignment.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(assignmentService, times(1)).removeById(assignment.getId());
    }

    @Test
    void getAssignmentDescription() {
        System.out.println(assignment.getId());
        //获取到全局 assignment 的 Id
        ResponseEntity<AssignmentController.DescriptionDTO> response = assignmentController.getAssignmentDescription(assignment.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getCourseAssignments() {
        //获取到全局 assignment 的 courseId
        ResponseEntity<List<Assignment>> response = assignmentController.getCourseAssignments(assignment.getCourseId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getAssignmentsSubmissions() {
        ResponseEntity<AssignmentController.AssignmentReviewListDTO> response = assignmentController.getAssignmentsSubmissions(assignment.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAssignmentsPeerReviewList() {
        ResponseEntity<List<AssignmentController.AssignmentSubmissionDetail>> response = assignmentController.getAssignmentsPeerReviewList(assignment.getId(), 22123456, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}