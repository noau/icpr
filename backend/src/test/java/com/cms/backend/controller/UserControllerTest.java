package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.DiscussionThread;
import com.cms.backend.pojo.Folder;
import com.cms.backend.pojo.TeacherInfo;
import com.cms.backend.pojo.User;
import com.cms.backend.service.*;
import com.cms.backend.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private FolderService folderService;

    @Mock
    private DiscussionThreadService discussionThreadService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testLoginUserNotFound() throws Exception {
        int userId = 22123456;
        String password = "123456";
        User user = new User(userId, password);
        when(userService.findById(userId)).thenReturn(null);
        mockMvc.perform(post("/user/login/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isGone());
    }

    @Test
    public void testLoginIncorrectPassword() throws Exception {
        // 模拟密码错误
        int userId = 22123456;
        String correctPassword = "123456";
        String wrongPassword = "123456789";
        User user = new User(userId, wrongPassword);
        User existingUser = new User(userId, correctPassword);
        when(userService.findById(userId)).thenReturn(existingUser);
        when(userService.getTeacherInfo(userId)).thenReturn(null);
        mockMvc.perform(post("/user/login/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isGone());
    }

    @Test
    public void testLoginUserNotStudent() throws Exception {
        // 模拟用户身份不是学生
        int userId = 22123456;
        String password = "123456";
        User user = new User(userId, password);
        User existingUser = new User(userId, password);
        when(userService.findById(userId)).thenReturn(existingUser);
        when(userService.getTeacherInfo(userId)).thenReturn(new TeacherInfo(11123456, "YF808", "副教授", "good"));
        mockMvc.perform(post("/user/login/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isGone());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // 模拟用户成功登录
        int userId = 22123456;
        String password = "123456";
        User user = new User(userId, password);

        User existingUser = new User(userId, password);
        when(userService.findById(userId)).thenReturn(existingUser);  // 模拟找到用户
        when(userService.getTeacherInfo(userId)).thenReturn(null);  // 模拟用户是学生

        // 模拟生成 JWT Token
        String token = "mockToken";
        when(JWTUtils.genToken(anyMap())).thenReturn(token);  // 模拟 JWT 生成

        mockMvc.perform(post("/user/login/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))  // 将对象转为 JSON 字符串
                .andExpect(status().isOk())  // 预期返回 200 状态
                .andExpect((ResultMatcher) content().string(token));  // 预期返回的 token 值
    }

    @Test
    void testGetUserInfo() throws Exception {
        Integer userId = 22123456;
        User user = new User(22123456, "123456");
        user.setName("张三");
        user.setUserClass("2206");
        user.setAcademy("软件");
        user.setGender("男");
        user.setAvatar("http://localhost:65/avatar.jpg");
        when(userService.findById(userId)).thenReturn(user);
        List<Folder> folderList = Arrays.asList(new Folder(1, userId, "Folder 1", "2024-10-04 11:39:52", 1, 0), new Folder(2, userId, "Folder 2", "2024-10-04 11:39:52", 0, 0));
        when(folderService.list(new LambdaQueryWrapper<Folder>().eq(Folder::getUserId, userId))).thenReturn(folderList);
        List<DiscussionThread> threadList = Arrays.asList(
                new DiscussionThread(1, "M310001B2计算机组成原理2024~2025上", userId, "Thread 1", "Content 1", 10, 5, 0, 0, "2024-10-04 11:39:52", "Tag 1", "2024-10-04 11:39:52"),
                new DiscussionThread(2, "M310001B2计算机组成原理2024~2025上", userId, "Thread 2", "Content 2", 20, 10, 0, 0, "2024-10-04 11:39:52", "Tag 2", "2024-10-04 11:39:52")
        );
        when(discussionThreadService.list(new LambdaQueryWrapper<DiscussionThread>().eq(DiscussionThread::getUserId, userId))).thenReturn(threadList);
        mockMvc.perform(get("/user/info?id=" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value("张三"))
                .andExpect((ResultMatcher) jsonPath("$.userClass").value("2206"))
                .andExpect((ResultMatcher) jsonPath("$.academy").value("软件"))
                .andExpect((ResultMatcher) jsonPath("$.gender").value("男"))
                .andExpect((ResultMatcher) jsonPath("$.avatar").value("http://localhost:65/avatar.jpg"))
                .andExpect((ResultMatcher) jsonPath("$.folderList.length()").value(2))
                .andExpect((ResultMatcher) jsonPath("$.threadList.length()").value(2))
                .andExpect((ResultMatcher) jsonPath("$.threadList[0].title").value("Thread 1"))
                .andExpect((ResultMatcher) jsonPath("$.threadList[1].title").value("Thread 2"));
    }

}