package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.Notification;
import com.cms.backend.service.NotificationService;
import com.cms.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
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
              notification.getUserId(),
                    notification.getTriggeredBy(),
                    notification.getType(),
                    notification.getRelatedId(),
                    notification.getContent(),
                    notification.getIsRead(),
                    notification.getCreatedAt(),
                    notification.getCourseId(),
                    notification.getIsStar(),
                    userService.findByUserName(notification.getUserId()).getName()
            );

            notifications.add(notificationDTO);
        }

        NotificationList notificationsList = new NotificationList(notifications);

        return ResponseEntity.ok(notificationsList);
    }

    @Data
    @AllArgsConstructor
    public static class NotificationDTO {

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
    public static class NotificationList {

        private List<NotificationDTO> notifications;

    }

}
