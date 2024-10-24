package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.Notification;
import com.cms.backend.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/publish")
    public ResponseEntity<String> publishNotification(@RequestBody Notification notification) {
        notificationService.save(notification);

        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/get")
    public ResponseEntity<NotificationList> getNotification(@RequestParam Integer id) {
        List<Notification> notifications = notificationService.list(new LambdaQueryWrapper<Notification>().eq(Notification::getUserId, id));
        NotificationList notificationList = new NotificationList(notifications);

        return ResponseEntity.ok(notificationList);
    }

    @Data
    @AllArgsConstructor
    public static class NotificationList {

        private List<Notification> notifications;

    }

}
