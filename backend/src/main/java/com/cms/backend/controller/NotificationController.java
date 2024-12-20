package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cms.backend.pojo.Notification;
import com.cms.backend.pojo.User;
import com.cms.backend.service.NotificationService;
import com.cms.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping(value = "/publish")
    public ResponseEntity<String> publishNotification(@RequestBody Notification notification) {
        notificationService.save(notification);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/get")
    public ResponseEntity<NotificationList> getNotification(@RequestParam Integer id) {
        // 将作业通知过滤掉
        List<NotificationDTO> notifications = new ArrayList<>();
        List<Notification> notificationList = notificationService.list(new LambdaQueryWrapper<Notification>().eq(Notification::getUserId, id));
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO(
                    notification.getId(),
                    notification.getUserId(),
                    notification.getTriggeredBy(),
                    notification.getType(),
                    notification.getRelatedId(),
                    notification.getContent(),
                    notification.getIsRead(),
                    notification.getCreatedAt(),
                    notification.getCourseId(),
                    notification.getIsStar(),
                    userService.findById(notification.getTriggeredBy()).getName(),
                    notification.getTitle()
            );

            notifications.add(notificationDTO);
        }

        NotificationList notificationsList = new NotificationList(notifications);

        return ResponseEntity.ok(notificationsList);
    }

    /**
     * 更新通知收藏信息
     *
     * @param notification 更新的通知收藏信息
     * @return 更新结果
     */
    @PostMapping("/star")
    public ResponseEntity<Void> updateCollectionNotification(@RequestBody NotificationDTO notification) {
        // 更新通知信息
        notificationService.update(
                new LambdaUpdateWrapper<Notification>()
                        .eq(Notification::getId, notification.getId())
                        .set(Notification::getIsStar, notification.getIsStar())
        );
        return ResponseEntity.ok().build();
    }

    /**
     * 更新通知已读信息
     *
     * @param notification 更新的通知已读信息
     * @return 更新结果
     */
    @PostMapping("/read")
    public ResponseEntity<Void> updateReadNotification(@RequestBody NotificationDTO notification) {
        // 更新通知信息
        notificationService.update(
                new LambdaUpdateWrapper<Notification>()
                        .eq(Notification::getId, notification.getId())
                        .set(Notification::getIsRead, notification.getIsRead())
        );
        return ResponseEntity.ok().build();
    }

    /**
     * 删除通知信息
     *
     * @param id 删除的通知信息ID
     * @return 删除结果
     */
    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> notificationDelete(@RequestParam Integer id) {
        System.out.println(id);
        notificationService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取所有收藏通知信息
     *
     * @param id 用户ID
     * @return 返回所有收藏通知
     */
    @GetMapping(value = "/get-all-collection")
    public ResponseEntity<NotificationList> getStarredNotifications(@RequestParam Integer id) {
        List<NotificationDTO> notifications = new ArrayList<>();
        // 查询指定用户的收藏通知（isStar = 1）
        List<Notification> starredNotificationList = notificationService.list(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, id)
                        .eq(Notification::getIsStar, 1) // 过滤收藏的通知
        );

        // 遍历收藏通知列表并转换为DTO
        for (Notification notification : starredNotificationList) {
            NotificationDTO notificationDTO = new NotificationDTO(
                    notification.getId(),
                    notification.getUserId(),
                    notification.getTriggeredBy(),
                    notification.getType(),
                    notification.getRelatedId(),
                    notification.getContent(),
                    notification.getIsRead(),
                    notification.getCreatedAt(),
                    notification.getCourseId(),
                    notification.getIsStar(),
                    userService.findById(notification.getTriggeredBy()).getName(),
                    notification.getTitle()
            );

            notifications.add(notificationDTO);
        }

        NotificationList notificationsList = new NotificationList(notifications);

        return ResponseEntity.ok(notificationsList);
    }

    /**
     * 获取指定类型的通知信息
     *
     * @param id   用户ID
     * @param type 通知类型
     * @return 返回指定类型的通知
     */
    @GetMapping(value = "/get-notification-by-type")
    public ResponseEntity<NotificationList> getStarredNotificationsByType(@RequestParam Integer id, @RequestParam String type) {
        List<NotificationDTO> notifications = new ArrayList<>();
        // 查询指定用户的收藏通知（isStar = 1）并根据类型过滤
        List<Notification> starredNotificationList = notificationService.list(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, id)
                        .eq(Notification::getType, type) // 根据类型过滤
        );

        // 遍历收藏通知列表并转换为DTO
        for (Notification notification : starredNotificationList) {
            NotificationDTO notificationDTO = new NotificationDTO(
                    notification.getId(),
                    notification.getUserId(),
                    notification.getTriggeredBy(),
                    notification.getType(),
                    notification.getRelatedId(),
                    notification.getContent(),
                    notification.getIsRead(),
                    notification.getCreatedAt(),
                    notification.getCourseId(),
                    notification.getIsStar(),
                    userService.findById(notification.getTriggeredBy()).getName(),
                    notification.getTitle()
            );

            notifications.add(notificationDTO);
        }

        NotificationList notificationsList = new NotificationList(notifications);
        return ResponseEntity.ok(notificationsList);
    }

    @Data
    @AllArgsConstructor
    public static class NotificationDTO {

        private Integer id;

        private Integer userId;

        private Integer triggeredBy;

        private String type;

        private Integer relatedId;

        private String content;

        private Integer isRead;

        private String createdAt;

        private String courseId;

        private Integer isStar;

        private String userName;

        private String title;

    }

    @Data
    @AllArgsConstructor
    public static class NotificationList {

        private List<NotificationDTO> notifications;

    }

}
