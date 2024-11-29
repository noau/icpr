package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.Attachment;
import com.cms.backend.pojo.Course;
import com.cms.backend.service.AttachmentFolderService;
import com.cms.backend.service.AttachmentService;
import com.cms.backend.service.CourseService;
import com.cms.backend.service.UserService;
import com.cms.backend.service.assignment.AssignmentService;
import com.cms.backend.service.assignment.AssignmentSubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private UserService userService;

    @Mock
    private AttachmentFolderService attachmentFolderService;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private AssignmentSubmissionService assignmentSubmissionService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void testGetAllCourse() throws Exception {
        Integer userId = 22123456;
        List<Course> courses = Arrays.asList(
                new Course("M310001B2计算机组成原理2024~2025上", "M310001B", "计算机组成原理", "2024~2025上", 2, "2024-09-01 00:00:00", "2024-12-31 00:00:00", "软件", "魏翔", "计算机组成原理"),
                new Course("M310003B3计算机网络2024~2025上", "M310003B", "计算机网络", "2024~2025上", 3, "2024-09-01 00:00:00", "2024-12-31 00:00:00", "软件", "鲁凌云", "计算机网络"),
                new Course("M310006B1机械学习2024~2025上", "M310006B", "机械学习", "2024~2025上", 1, "2024-09-01 00:00:00", "2024-12-31 00:00:00", "软件", "鲍鹏", "机械学习")
        );
        when(courseService.getAllCourse(userId)).thenReturn(courses);
        mockMvc.perform(get("/courses/all?id=" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses.length()").value(3))
                .andExpect(jsonPath("$.courses[0].name").value("计算机组成原理"))
                .andExpect(jsonPath("$.courses[1].name").value("计算机网络"))
                .andExpect(jsonPath("$.courses[2].name").value("机械学习"));
    }

    @Test
    void testGetAllCourseTeacher() throws Exception {
        Integer teacherId = 11123456;
        List<Course> courses = Arrays.asList(
                new Course("M310001B2计算机组成原理2024~2025上", "M310001B", "计算机组成原理", "2024~2025上", 2, "2024-09-01 00:00:00", "2024-12-31 00:00:00", "软件", "魏翔", "计算机组成原理"),
                new Course("M310003B3计算机网络2024~2025上", "M310003B", "计算机网络", "2024~2025上", 3, "2024-09-01 00:00:00", "2024-12-31 00:00:00", "软件", "鲁凌云", "计算机网络"),
                new Course("M310006B1机械学习2024~2025上", "M310006B", "机械学习", "2024~2025上", 1, "2024-09-01 00:00:00", "2024-12-31 00:00:00", "软件", "鲍鹏", "机械学习")
        );
        when(courseService.getAllCourseTeacher(teacherId)).thenReturn(courses);
        mockMvc.perform(get("/courses/all-teach?id=" + teacherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses.length()").value(3))  // 验证返回的课程列表长度为3
                .andExpect(jsonPath("$.courses[0].name").value("计算机组成原理"))  // 验证第一个课程的名称
                .andExpect(jsonPath("$.courses[1].name").value("计算机网络"))  // 验证第二个课程的名称
                .andExpect(jsonPath("$.courses[2].name").value("机械学习"));  // 验证第三个课程的名称
    }

    @Test
    void testGetCourseInfo() throws Exception {
        String courseId = "22123456";
        Course course = new Course("M310001B2计算机组成原理2024~2025上", "M310001B", "计算机组成原理", "2024~2025上", 2, "2024-09-01 00:00:00", "2024-12-31 00:00:00", "软件", "魏翔", "计算机组成原理");
        when(courseService.getById(courseId)).thenReturn(course);

        mockMvc.perform(get("/courses/get-info")
                        .param("id", courseId))  // 传入课程 ID 参数
                .andExpect(status().isOk())  // 验证状态码为 200 OK
                .andExpect(jsonPath("$.id").value("M310001B2计算机组成原理2024~2025上"))
                .andExpect(jsonPath("$.name").value("计算机组成原理"));
    }

    @Test
    void testGetExam() throws Exception {
        String id = "M310001B2计算机组成原理2024~2025上";
        Attachment attachment1 = new Attachment();
        attachment1.setId(1);
        attachment1.setAttachmentFolderId(null);
        attachment1.setExamId(id);
        Attachment attachment2 = new Attachment();
        attachment2.setId(2);
        attachment2.setAttachmentFolderId(null);
        attachment2.setExamId(id);
        List<Attachment> attachments = Arrays.asList(attachment1, attachment2);
        when(attachmentService.list(new LambdaQueryWrapper<Attachment>().eq(Attachment::getExamId, id))).thenReturn(attachments);
        ResultActions result = mockMvc.perform(get("/courses/get-exam")
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.attachments[0].id").value(1));
    }

    @Test
    void testCreateAttachmentFolder() throws Exception {
        // 创建一个模拟的 AttachmentFolder 对象
        CourseController.AttachmentFolder attachmentFolder = new CourseController.AttachmentFolder(null, "Test Folder", null, "M310001B2计算机组成原理2024~2025上", "exam");

        attachmentFolder.setId(1);
        // 模拟 courseService.createAttachmentFolder 调用
        doNothing().when(courseService).createAttachmentFolder(attachmentFolder);

        // 模拟调用 createAttachmentFolder 后，attachmentFolder 的 ID 被设置为 1
        attachmentFolder.setId(1); // 设置返回的 ID

        // 执行 POST 请求并传入 attachmentFolder 对象
        ResultActions result = mockMvc.perform(post("/courses/create-attachment-folder")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, " +
                        "\"folderName\": \"Test Folder\", " +
                        "\"parentId\": null, " +
                        "\"courseId\": \"M310001B2计算机组成原理2024~2025上\", " +
                        "\"content\": \"Test Content\", " +
                        "\"type\": \"exam\"}"));
        // 验证返回结果
        result.andExpect(status().isOk())
                .andExpect(content().string("1")); // 验证返回的 ID 是 1
    }

}