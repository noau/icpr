package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.cms.backend.pojo.Notification;
import com.cms.backend.pojo.User;
import com.cms.backend.service.NotificationService;
import com.cms.backend.service.UserService;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Notification.class);
    }

    @Test
    void publishNotification() {
        Notification notification = new Notification(1, 22123456, "测试通知", 21123456, "作业通知", 0, "测试中", 0, "2024-11-28", "M310007B2软件测试与质量保证2024~2025上", 0);
        notificationController.publishNotification(notification);

        // 验证 service 是否被调用
        verify(notificationService, times(1)).save(notification);
    }

    @Test
    void getNotification() {
        Integer userId = 22123456;

        // 模拟返回的通知列表
        List<Notification> mockNotifications = List.of(
                new Notification(1, 22123456, "测试通知", 21123456, "作业通知", 0, "测试中", 0, "2024-11-28", "M310007B2软件测试与质量保证2024~2025上", 0)
        );

        when(notificationService.list((Wrapper<Notification>) any())).thenReturn(mockNotifications);
        // 模拟返回的 User 对象
        User mockUser = new User();
        mockUser.setName("李四");

        when(notificationService.list((Wrapper<Notification>) any())).thenReturn(mockNotifications);
        when(userService.findById(21123456)).thenReturn(mockUser);

        // 调用测试方法
        ResponseEntity<NotificationController.NotificationList> response = notificationController.getNotification(userId);

        // 验证返回值
        assertEquals(1, Objects.requireNonNull(response.getBody()).getNotifications().size());
        assertEquals("李四", response.getBody().getNotifications().getFirst().getUserName());

        // 验证服务层方法是否被调用
        verify(notificationService, times(1)).list((Wrapper<Notification>) any());
        verify(userService, times(1)).findById(21123456);
    }

    @Test
    void updateCollectionNotification() {
        // 准备测试数据
        NotificationController.NotificationDTO notificationDTO = new NotificationController.NotificationDTO(
                1, 22123456, 21123456, "作业通知", 0, "内容", 0, "2024-11-28", "M310007B2软件测试与质量保证2024~2025上", 1, "李四", "是否已读"
        );

        // 调用被测方法
        notificationController.updateCollectionNotification(notificationDTO);

        // 捕获传递给 service 的 LambdaUpdateWrapper
        ArgumentCaptor<LambdaUpdateWrapper> captor = ArgumentCaptor.forClass(LambdaUpdateWrapper.class);
        verify(notificationService, times(1)).update(captor.capture());

        // 获取捕获的 SQL 片段和参数
        LambdaUpdateWrapper<Notification> capturedWrapper = captor.getValue();

        // 验证 SQL 片段是否包含预期字段
        assertTrue(capturedWrapper.getSqlSegment().contains("id"));

        // 验证参数绑定
        assertEquals(1, capturedWrapper.getParamNameValuePairs().get("MPGENVAL2"));
    }


    @Test
    void updateReadNotification() {
        NotificationController.NotificationDTO notificationDTO = new NotificationController.NotificationDTO(
                1, 22123456, 21123456, "作业通知", 0, "内容", 0, "2024-11-28", "M310007B2软件测试与质量保证2024~2025上", 0, "李四", "是否已读"
        );

        notificationController.updateReadNotification(notificationDTO);

        ArgumentCaptor<LambdaUpdateWrapper> captor = ArgumentCaptor.forClass(LambdaUpdateWrapper.class);
        verify(notificationService, times(1)).update(captor.capture());

        LambdaUpdateWrapper<Notification> capturedWrapper = captor.getValue();
        System.out.println(capturedWrapper.getSqlSegment());

        // 验证条件设置
        assertTrue(capturedWrapper.getSqlSegment().contains("id"));
        assertTrue(capturedWrapper.getParamNameValuePairs().containsValue(1));
    }

    @Test
    void notificationDelete() {
        Integer notificationId = 1;

        notificationController.notificationDelete(notificationId);

        verify(notificationService, times(1)).removeById(notificationId);
    }

    @Test
    void getStarredNotifications() {
        Integer userId = 22123456;

        // 模拟返回的收藏通知列表
        List<Notification> mockNotifications = List.of(
                new Notification(1, 22123456, "测试通知", 21123456, "作业通知", 0, "测试中", 0, "2024-11-28", "M310007B2软件测试与质量保证2024~2025上", 0)
        );

        when(notificationService.list((Wrapper<Notification>) any())).thenReturn(mockNotifications);
        // 模拟返回的 User 对象
        User mockUser = new User();
        mockUser.setName("李四");

        when(notificationService.list((Wrapper<Notification>) any())).thenReturn(mockNotifications);
        when(userService.findById(21123456)).thenReturn(mockUser);


        ResponseEntity<NotificationController.NotificationList> response = notificationController.getStarredNotifications(userId);

        assertEquals(1, Objects.requireNonNull(response.getBody()).getNotifications().size());
        assertEquals("李四", response.getBody().getNotifications().getFirst().getUserName());

        // 验证服务层方法是否被调用
        verify(notificationService, times(1)).list((Wrapper<Notification>) any());
        verify(userService, times(1)).findById(21123456);
    }

    @Test
    void getStarredNotificationsByType() {
        Integer userId = 22123456;

        // 模拟返回的收藏通知列表
        List<Notification> mockNotifications = List.of(
                new Notification(1, 22123456, "测试通知", 21123456, "作业通知", 0, "测试中", 0, "2024-11-28", "M310007B2软件测试与质量保证2024~2025上", 0)
        );

        when(notificationService.list((Wrapper<Notification>) any())).thenReturn(mockNotifications);
        // 模拟返回的 User 对象
        User mockUser = new User();
        mockUser.setName("李四");

        when(notificationService.list((Wrapper<Notification>) any())).thenReturn(mockNotifications);
        when(userService.findById(21123456)).thenReturn(mockUser);

        ResponseEntity<NotificationController.NotificationList> response = notificationController.getStarredNotificationsByType(userId, "作业通知");

        assertEquals(1, Objects.requireNonNull(response.getBody()).getNotifications().size());
        assertEquals("李四", response.getBody().getNotifications().getFirst().getUserName());

        // 验证服务层方法是否被调用
        verify(notificationService, times(1)).list((Wrapper<Notification>) any());
        verify(userService, times(1)).findById(21123456);
    }
}