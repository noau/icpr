package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cms.backend.pojo.Notification;
import com.cms.backend.service.NotificationService;
import com.cms.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
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
                    userService.findByUserName(notification.getTriggeredBy()).getName()
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
    public ResponseEntity<Void> updateCollectionNotification(@RequestBody Notification notification) {
        // 创建一个更新对象
        UpdateNotificationDTO notificationDTO = new UpdateNotificationDTO(
                notification.getId(),
                notification.getIsRead(),
                notification.getIsStar()
        );

        // 更新通知信息
        notificationService.update(
                new LambdaUpdateWrapper<Notification>()
                        .eq(Notification::getId, notificationDTO.getId())
                        .set(Notification::getIsStar, notificationDTO.getIsStar())
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
    public ResponseEntity<Void> updateReadNotification(@RequestBody Notification notification) {
        // 创建一个更新对象
        UpdateNotificationDTO notificationDTO = new UpdateNotificationDTO(
                notification.getId(),
                notification.getIsRead(),
                notification.getIsStar()
        );

        // 更新通知信息
        notificationService.update(
                new LambdaUpdateWrapper<Notification>()
                        .eq(Notification::getId, notificationDTO.getId())
                        .set(Notification::getIsRead, notificationDTO.getIsRead())
        );
        return ResponseEntity.ok().build();
    }

    //删除通知
    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> notificationDelete(@RequestParam Integer id) {
        System.out.println(id);
        notificationService.removeById(id);
        return ResponseEntity.noContent().build();
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

    }
    @Data
    @AllArgsConstructor
    public static class UpdateNotificationDTO {
        private Integer id;

        private Integer isRead;

        private Integer isStar;
    }
    @Data
    @AllArgsConstructor
    public static class NotificationList {

        private List<NotificationDTO> notifications;

    }

}
