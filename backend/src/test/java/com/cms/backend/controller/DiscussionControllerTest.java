package com.cms.backend.controller;

import com.cms.backend.pojo.DiscussionThread;
import com.cms.backend.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DiscussionControllerTest {

    @InjectMocks
    private DiscussionController discussionController;

    @Mock
    private DiscussionThreadService discussionThreadService;

    @Mock
    private DiscussionReplyService discussionReplyService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private DiscussionLikeService discussionLikeService;

    @Mock
    private DiscussionCollectService discussionCollectService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(discussionController).build();
    }

    @Test
    public void testCreateThread() throws Exception {
        mockMvc.perform(post("/discussion/thread")
                        .contentType("application/json")
                        .content("{\"courseId\": \"M310001B2计算机组成原理2024~2025上\", " +
                                "\"userId\": 22123456, " +
                                "\"title\": \"Test Title\", " +
                                "\"content\": \"Test Content\", " +
                                "\"closed\": 0, " +
                                "\"top\": 0, " +
                                "\"createdAt\": \"2024-11-28T10:00:00\", " +
                                "\"tag\": \"Test Tag\", " +
                                "\"updatedAt\": \"2024-11-28T10:00:00\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateReplyForThread() throws Exception {
        mockMvc.perform(post("/discussion/replies")
                        .contentType("application/json")
                        .content("{\"threadId\": 1, " +
                                "\"replyId\": 0, " +
                                "\"userId\": 22123456, " +
                                "\"content\": \"Test Content\", " +
                                "\"createdAt\": \"2024-11-28T10:00:00\", " +
                                "\"repliedId\": null}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateReplyForReply() throws Exception {
        mockMvc.perform(post("/discussion/replies")
                        .contentType("application/json")
                        .content("{\"threadId\": 0, " +
                                "\"replyId\": 1, " +
                                "\"userId\": 22123456, " +
                                "\"content\": \"Test Content\", " +
                                "\"createdAt\": \"2024-11-28T10:00:00\", " +
                                "\"repliedId\": 3}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCourseThreadList() throws Exception {
        mockMvc.perform(get("/discussion/course?id=M310001B2计算机组成原理2024~2025上&userId=22123456"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetThreadInfo() throws Exception {
        DiscussionThread mockThread = new DiscussionThread(1,
                "M310001B2计算机组成原理2024~2025上",
                21123456,
                "原码一位乘法",
                "符号位单独参加运算，数据位取绝对值，每次将一位乘数对应的部分积与原部分积的累加和进行相加，并右移一位，直到乘数的所有位被用完。",
                1,
                1,
                0,
                0,
                "2024-10-04 11:39:52",
                "期中复习",
                "2024-11-18 21:35:47");
        mockThread.setUserId(22123456);
        Mockito.when(discussionThreadService.getById(1)).thenReturn(mockThread);
        mockMvc.perform(get("/discussion/get-thread?id=1&userId=22123456"))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeThread() throws Exception {
        mockMvc.perform(post("/discussion/change")
                        .contentType("application/json")
                        .content("{\"courseId\": \"M310001B2计算机组成原理2024~2025上\", " +
                                "\"userId\": 22123456, " +
                                "\"title\": \"Test Title\", " +
                                "\"content\": \"Test Content\", " +
                                "\"closed\": 0, " +
                                "\"top\": 0, " +
                                "\"createdAt\": \"2024-11-28T10:00:00\", " +
                                "\"tag\": \"Test Tag\", " +
                                "\"updatedAt\": \"2024-11-28T10:00:00\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testThreadDelete() throws Exception {
        mockMvc.perform(delete("/discussion/thread-delete?id=1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testReplyDelete() throws Exception {
        mockMvc.perform(delete("/discussion/reply-delete?id=1"))
                .andExpect(status().isNoContent());
    }

}