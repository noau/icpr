package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Validated
@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentSubmissionService assignmentSubmissionService;
    private final JavaMailSender mailSender;
    private final AttachmentService attachmentService;
    private final AssignmentPeerReviewService assignmentPeerReviewService;
    private final AssignmentReviewService assignmentReviewService;
    private final NotificationService notificationService;
    private final CourseService courseService;
    private final UserService userService;
    private final ThreadPoolTaskScheduler taskScheduler;

    private final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.nickname}")
    private String nickname;

    public AssignmentController(AssignmentService assignmentService, AssignmentSubmissionService assignmentSubmissionService, JavaMailSender mailSender, AttachmentService attachmentService, AssignmentPeerReviewService assignmentPeerReviewService, AssignmentReviewService assignmentReviewService, NotificationService notificationService, CourseService courseService, UserService userService, ThreadPoolTaskScheduler taskScheduler) {
        this.assignmentService = assignmentService;
        this.assignmentSubmissionService = assignmentSubmissionService;
        this.mailSender = mailSender;
        this.attachmentService = attachmentService;
        this.assignmentPeerReviewService = assignmentPeerReviewService;
        this.assignmentReviewService = assignmentReviewService;
        this.notificationService = notificationService;
        this.courseService = courseService;
        this.userService = userService;
        this.taskScheduler = taskScheduler; // 确保注入 taskScheduler
    }

    /**
     * 发布作业 + 作业通知
     *
     * @param assignment 作业信息
     * @return 发布作业结果
     */
    @PostMapping("/issue")
    public ResponseEntity<String> issueAssignment(@RequestBody AssignmentIssue assignment) {
        var newAssignment = new Assignment(0, assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(), assignment.getStart(), assignment.getEnd(), assignment.getIsPrivate(), assignment.getFullGrade(), assignment.getDelayedGrade(), assignment.getLatestEnd(), assignment.getMultipleSubmission(), assignment.getPublishGrade(), assignment.getRequirePeerReview(), assignment.getPeerReviewStart(), assignment.getPeerReviewEnd(), assignment.getMinPeerReview(), assignment.getAnswer());

        assignmentService.save(newAssignment);

        // 更新附件信息
        assignment.getAttachments().forEach(attachment -> attachmentService.update(new LambdaUpdateWrapper<Attachment>().eq(Attachment::getId, attachment).set(Attachment::getAssignmentId, newAssignment.getId())));

        List<User> users = courseService.getAllStudents(assignment.courseId);
        sendNotification(users, newAssignment, "新作业来啦~", assignment.courseId + "作业已发布，请尽快完成");

        //设置截止日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String endString = assignment.getEnd();
        String latestEndString = assignment.getLatestEnd();
        String peerReviewStartString = assignment.getPeerReviewStart();
        String peerReviewEndString = assignment.getPeerReviewEnd();
        // 将字符串转为 LocalDateTime 类型
        LocalDateTime end = LocalDateTime.parse(endString, formatter);
        LocalDateTime latestEnd = LocalDateTime.parse(latestEndString, formatter);
        LocalDateTime peerReviewStart = LocalDateTime.parse(peerReviewStartString, formatter);
        LocalDateTime peerReviewEnd = LocalDateTime.parse(peerReviewEndString, formatter);
        // 设置提醒任务
        scheduleReminder(end.minusDays(1), users, newAssignment, "作业截止提醒", "作业截止时间剩余1天，请尽快提交");
        scheduleReminder(latestEnd.minusDays(1), users, newAssignment, "最迟补交提醒", "最迟补交时间剩余1天，请尽快完成");
        scheduleReminder(peerReviewStart.minusDays(1), users, newAssignment, "互评即将开始", "互评即将开始,不要忘记");
        scheduleReminder(peerReviewEnd.minusDays(1), users, newAssignment, "互评截止提醒", "互评截止时间剩余1天，请尽快完成互评");

        return ResponseEntity.ok("作业发布成功，已通知所有学生。");
    }

    /**
     * 设置提醒
     */
    private void scheduleReminder(LocalDateTime reminderTime, List<User> users, Assignment assignment, String title, String content) {
        System.out.println("发送提醒通知: " + title + " 给学生");
        // 将 reminderTime 转换为 Instant
        Instant scheduleTime = reminderTime.atZone(ZoneId.systemDefault()).toInstant();
        taskScheduler.schedule(() -> sendNotification(users, assignment, title, content), scheduleTime);
    }

    /**
     * 发送提醒通知
     */
    private void sendNotification(List<User> users, Assignment assignment, String title, String content) {
        System.out.println("发送作业通知: " + title + " 给学生" + content);
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        TeachingDTO teachingDTO = courseService.getTeacherId(assignment.getCourseId());

        for (User user : users) {
            Notification notification = new Notification(user.getId(), title, teachingDTO.getTeacherId(), "作业通知", assignment.getId(), content, 0, formattedNow, assignment.getCourseId(), 0);
            notificationService.save(notification);
            //同步转发到email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(nickname + '<' + sender + '>');
            message.setTo(user.getEmail());
            message.setSubject(assignment.getCourseId() + "作业通知");
            message.setText(content);
            mailSender.send(message);
        }
    }

    /**
     * 批改作业
     *
     * @param assignmentReview 作业批改信息
     * @return 批改作业结果
     */
    @PostMapping("/reviews")
    public ResponseEntity<Void> reviewAssignment(@RequestBody AssignmentReview assignmentReview) {
        assignmentReviewService.saveOrUpdate(assignmentReview);
        logger.info("Reviewed assignment ID: {}", assignmentReview.getSubmissionId());
        if (assignmentReview.getGrade() / assignmentService.getById(assignmentSubmissionService.getById(assignmentReview.getSubmissionId()).getAssignmentId()).getFullGrade() > 0.9) {
            userService.addMark(assignmentSubmissionService.getById(assignmentReview.getSubmissionId()).getStudentId(), 2);
        } else if (assignmentReview.getGrade() / assignmentService.getById(assignmentSubmissionService.getById(assignmentReview.getSubmissionId()).getAssignmentId()).getFullGrade() > 0.8) {
            userService.addMark(assignmentSubmissionService.getById(assignmentReview.getSubmissionId()).getStudentId(), 1);
        } else if (assignmentReview.getGrade() / assignmentService.getById(assignmentSubmissionService.getById(assignmentReview.getSubmissionId()).getAssignmentId()).getFullGrade() < 0.6) {
            userService.dropMark(assignmentSubmissionService.getById(assignmentReview.getSubmissionId()).getStudentId(), 1);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 提交作业
     *
     * @param assignmentSubmissionDTO 作业提交信息
     * @return 提交作业结果
     */
    @PostMapping("/submissions")
    public ResponseEntity<Void> submitAssignment(@RequestBody AssignmentSubmissionDTO assignmentSubmissionDTO) {
        var submission = new AssignmentSubmission(null, assignmentSubmissionDTO.getAssignmentId(), assignmentSubmissionDTO.getStudentId(), assignmentSubmissionDTO.getSubmittedAt(), assignmentSubmissionDTO.getContent());
        var checkSubmission = assignmentSubmissionService.getOne(new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, assignmentSubmissionDTO.getAssignmentId()).eq(AssignmentSubmission::getStudentId, assignmentSubmissionDTO.getStudentId()));
        if (checkSubmission == null) {
            assignmentSubmissionService.save(submission);
            assignmentSubmissionDTO.getAttachments().forEach(attachment -> attachmentService.update(new LambdaUpdateWrapper<Attachment>().eq(Attachment::getId, attachment).set(Attachment::getSubmissionId, submission.getId())));
            int submissionNumber = assignmentSubmissionService.list(new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, assignmentSubmissionDTO.assignmentId)).size();
            int allStudentNumber = courseService.getAllStudents(assignmentService.getOne(new LambdaQueryWrapper<Assignment>().eq(Assignment::getId, assignmentSubmissionDTO.getAssignmentId())).getCourseId()).size();
            float rate = (float) submissionNumber / allStudentNumber;
            if (rate < 0.1) {
                userService.addMark(assignmentSubmissionDTO.studentId, 2);
            } else if (rate < 0.7) {
                userService.addMark(assignmentSubmissionDTO.studentId, 1);
            }
        } else {
            assignmentSubmissionService.update(submission, new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, assignmentSubmissionDTO.getAssignmentId()).eq(AssignmentSubmission::getStudentId, assignmentSubmissionDTO.getStudentId()));
        }

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
        if (peerReview.getFeedback() != null && peerReview.getFeedback().length() > 100) {
            userService.addMark(peerReview.getReviewerId(), 1);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 公布作业答案 + 通知提醒
     *
     * @param answer 答案
     * @return 公布作业答案结果
     */
    @PostMapping("/issue-answer")
    public ResponseEntity<Void> issueAnswer(@RequestBody IssueAnswer answer) {
        assignmentService.update(new LambdaUpdateWrapper<Assignment>().eq(Assignment::getId, answer.assignmentId).set(Assignment::getAnswer, answer.content));
        answer.attachments.forEach(attachment -> attachmentService.update(new LambdaUpdateWrapper<Attachment>().eq(Attachment::getId, attachment).set(Attachment::getAnswerId, answer.assignmentId)));
        var assignment = assignmentService.getById(answer.assignmentId);
        //获得该课程所有学生
        List<User> users = courseService.getAllStudents(assignment.getCourseId());
        sendNotification(users, assignment, "作业公布答案", assignment.getCourseId() + assignment.getTitle() + "的答案已经公布");
        return ResponseEntity.ok().build();
    }

    /**
     * 修改作业 + 作业通知
     *
     * @param assignment 作业信息
     * @return 修改作业结果
     */
    @PostMapping("/change")
    public ResponseEntity<Void> changeAssignment(@RequestBody AssignmentIssue assignment) {
        var newAssignment = new Assignment(assignment.getId(), assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(), assignment.getStart(), assignment.getEnd(), assignment.getIsPrivate(), assignment.getFullGrade(), assignment.getDelayedGrade(), assignment.getLatestEnd(), assignment.getMultipleSubmission(), assignment.getPublishGrade(), assignment.getRequirePeerReview(), assignment.getPeerReviewStart(), assignment.getPeerReviewEnd(), assignment.getMinPeerReview(), assignment.getAnswer());
        assignmentService.updateById(newAssignment);
        assignment.getAttachments().forEach(attachment -> attachmentService.update(new LambdaUpdateWrapper<Attachment>().eq(Attachment::getId, attachment).set(Attachment::getAssignmentId, newAssignment.getId())));
        //获得该课程所有学生
        List<User> users = courseService.getAllStudents(assignment.getCourseId());
        sendNotification(users, newAssignment, "作业被教师更新", assignment.getCourseId() + assignment.getTitle() + "的内容已经被更新");
        return ResponseEntity.ok().build();
    }

    /**
     * 删除作业 + 作业通知
     *
     * @param id 作业ID
     * @return 删除作业结果
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAssignment(@RequestParam Integer id) {
        //通过id查询到对应作业
        var assignment = assignmentService.getById(id);
        //获得该课程所有学生
        List<User> users = courseService.getAllStudents(assignment.getCourseId());
        sendNotification(users, assignment, "作业被教师删除", assignment.getCourseId() + assignment.getTitle() + "已经被任课老师删除");
        //删除作业
        assignmentService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获得作业详情
     *
     * @param studentId    作业ID
     * @param assignmentId 作业ID
     * @return 获得作业详情结果
     */
    @GetMapping("/submission")
    public ResponseEntity<SubmissionInfo> getSubmissionInfo(@RequestParam Integer studentId, @RequestParam Integer assignmentId) {

        AssignmentSubmission assignmentSubmission = assignmentSubmissionService.getOne(new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, assignmentId).eq(AssignmentSubmission::getStudentId, studentId));
        AssignmentReview assignmentReview = assignmentReviewService.findAllBySubmissionId(assignmentSubmission.getId());
        List<Attachment> attachments = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getSubmissionId, assignmentSubmission.getId()));
        var attachmentInfos = attachments.stream().map(attachment -> new AttachmentListDTO(attachment.getId(), attachment.getName())).toList();

        SubmissionInfo submissionInfo = new SubmissionInfo(studentId,
                assignmentSubmission.getSubmittedAt(),
                assignmentReview == null ? null : assignmentReview.getGrade(),
                assignmentSubmission.getContent(),
                assignmentReview == null ? null : assignmentReview.getFeedback(),
                assignmentReview == null ? null : assignmentReview.getGradedAt(),
                attachmentInfos);
        return ResponseEntity.ok(submissionInfo);
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
            List<Integer> attachmentIds = attachments.stream().map(Attachment::getId).collect(Collectors.toList());
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

    /**
     * 批改列表
     *
     * @param id 课程ID
     * @return 获得批改作业列表
     */
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
        var details = assignmentSubmissionService.getPeerReviewSubmissions(assignmentId, userId).stream().filter(submission -> !Objects.equals(submission.getStudentId(), userId)).map(submission -> {
            var attachments = attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getSubmissionId, submission.getId()).select(Attachment::getId)).stream().map(Attachment::getId).toList();
            return new AssignmentSubmissionDetail(submission, attachments);
        }).collect(Collectors.toList());
        var review_count = Math.min(count, details.size());
        return ResponseEntity.ok(splitAndRetrieve(details, review_count));
    }


    @GetMapping("/course-assignments/student")
    public ResponseEntity<List<AssignmentStudent>> getCourseAssignmentsStudent(@RequestParam String id, Integer userId) {
        List<Assignment> assignmentList = assignmentService.list(new LambdaQueryWrapper<Assignment>().eq(Assignment::getCourseId, id));
        List<AssignmentStudent> assignmentStudentList = new ArrayList<>();
        for (Assignment assignment : assignmentList) {

            Float grade = null;
            var submitted = false;
            var assignmentSubmission = assignmentSubmissionService.getOne(new LambdaQueryWrapper<AssignmentSubmission>().eq(AssignmentSubmission::getAssignmentId, assignment.getId()).eq(AssignmentSubmission::getStudentId, userId));
            if (assignmentSubmission != null) {
                submitted = true;
                var assignmentReview = assignmentReviewService.getById(assignmentSubmission.getId());
                if (assignmentReview != null) {
                    grade = assignmentReview.getGrade();
                }
            }


            var peerReviewCount = assignmentPeerReviewService.getPeerReviewCount(userId, assignment.getId());
            AssignmentStudent assignmentStudent = new AssignmentStudent(
                    assignment.getId(),
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
                    peerReviewCount >= assignment.getMinPeerReview(),
                    submitted
            );

            assignmentStudentList.add(assignmentStudent);
        }

        return ResponseEntity.ok(assignmentStudentList);
    }

    public static <T> List<T> splitAndRetrieve(List<T> inputList, int blockCount) {
        List<T> result = new ArrayList<>();
        int totalSize = inputList.size();

        // Calculate block size for each block, remainder are distributed among the first few blocks
        int baseBlockSize = totalSize / blockCount;
        int remainder = totalSize % blockCount;

        int startIndex = 0;
        for (int i = 0; i < blockCount; i++) {
            // The size of this block will be the baseBlockSize plus 1 if there's still a remainder left
            int currentBlockSize = baseBlockSize + (remainder > 0 ? 1 : 0);
            remainder--;

            // If the block has elements, add the first element to the result
            if (currentBlockSize > 0 && startIndex < totalSize) {
                result.add(inputList.get(startIndex));
            }

            // Move to the next block
            startIndex += currentBlockSize;
        }

        return result;
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

        private boolean submitted;

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

    @Data
    @AllArgsConstructor
    public static class SubmissionInfo {
        Integer studentId;
        String submittedAt;
        Float grade;
        String content;
        String feedback;
        String gradedAt;
        private List<AttachmentListDTO> attachments;
    }

}
