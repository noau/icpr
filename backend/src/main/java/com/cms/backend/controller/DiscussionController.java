package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.DTO.FollowDTO;
import com.cms.backend.pojo.*;
import com.cms.backend.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    private final DiscussionThreadService discussionThreadService;

    private final DiscussionReplyService discussionReplyService;

    private final UserService userService;

    private final NotificationService notificationService;

    private final DiscussionLikeService discussionLikeService;

    private final DiscussionCollectService discussionCollectService;

    public DiscussionController(DiscussionThreadService discussionThreadService, DiscussionReplyService discussionReplyService, UserService userService, NotificationService notificationService, DiscussionLikeService discussionLikeService, DiscussionCollectService discussionCollectService) {
        this.discussionThreadService = discussionThreadService;
        this.discussionReplyService = discussionReplyService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.discussionLikeService = discussionLikeService;
        this.discussionCollectService = discussionCollectService;
    }

    @PostMapping(value = "/thread")
    public ResponseEntity<String> createThread(@RequestBody DiscussionThread discussionThread) {
        discussionThreadService.save(discussionThread);
        userService.addThreadNumber(discussionThread.getUserId());
        User user = userService.findById(discussionThread.getUserId());
        List<FollowDTO> users = userService.getUserFollowers(discussionThread.getUserId());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        for (FollowDTO followDTO : users) {
            Notification notification = new Notification(followDTO.getFollowingId(), "你的关注发帖啦！", discussionThread.getUserId(), "讨论区", discussionThread.getId(), "你的关注" + user.getName() + "发帖啦！快去看看吧~", 0, formattedNow, discussionThread.getCourseId(), 0);
            notificationService.save(notification);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/replies")
    public ResponseEntity<String> createReply(@RequestBody DiscussionReply discussionReply) {
        if (discussionReply.getThreadId() == 0) {
            DiscussionReply discussionReplyReply = new DiscussionReply(null, null, discussionReply.getReplyId(), discussionReply.getUserId(), discussionReply.getContent(), 0, discussionReply.getCreatedAt(), discussionReply.getRepliedId());
            discussionReplyService.save(discussionReplyReply);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);
            DiscussionReply discussionReplyUser = discussionReplyService.getById(discussionReply.getRepliedId());
            DiscussionThread discussionThread = discussionThreadService.getById(discussionReply.getReplyId());
            Notification notification = new Notification(discussionReplyUser.getUserId(), "收到评论！", discussionReply.getUserId(), "讨论区", discussionReplyReply.getId(), "你的评论被回复啦~", 0, formattedNow, discussionThread.getCourseId(), 0);
            notificationService.save(notification);
        } else {
            discussionReplyService.save(discussionReply);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);
            DiscussionThread discussionThread = discussionThreadService.getById(discussionReply.getThreadId());
            Notification notification = new Notification(discussionThread.getUserId(), "收到评论！", discussionReply.getUserId(), "讨论区", discussionReply.getId(), "你的帖子被回复啦~", 0, formattedNow, discussionThread.getCourseId(), 0);
            notificationService.save(notification);
        }

        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/course")
    public ResponseEntity<DiscussionThreadList> getCourse(@RequestParam String id, Integer userId) {
        List<DiscussionThread> discussionThreads = discussionThreadService.list(new LambdaQueryWrapper<DiscussionThread>().eq(DiscussionThread::getCourseId, id));
        List<DiscussionThreadDTO> discussionThreadDTOS = new ArrayList<>();
        for (DiscussionThread discussionThread : discussionThreads) {
            DiscussionLike discussionLike = discussionLikeService.getOne(new LambdaQueryWrapper<DiscussionLike>().eq(DiscussionLike::getThreadId, discussionThread.getId()).eq(DiscussionLike::getUserId, userId));
            DiscussionCollect discussionCollect = discussionCollectService.getOne(new LambdaQueryWrapper<DiscussionCollect>().eq(DiscussionCollect::getThreadId, discussionThread.getId()).eq(DiscussionCollect::getUserId, userId));
            DiscussionThreadDTO discussionThreadDTO = new DiscussionThreadDTO(discussionThread.getId(),
                    discussionThread.getCourseId(),
                    discussionThread.getUserId(),
                    discussionThread.getTitle(),
                    discussionThread.getContent(),
                    discussionThread.getLikes(),
                    discussionThread.getFavorites(),
                    discussionThread.getClosed(),
                    discussionThread.getTop(),
                    discussionThread.getCreatedAt(),
                    discussionThread.getTag(),
                    discussionThread.getUpdatedAt(),
                    discussionLike != null,
                    discussionCollect != null
            );

            discussionThreadDTOS.add(discussionThreadDTO);
        }
        DiscussionThreadList discussionThreadList = new DiscussionThreadList(discussionThreadDTOS);

        return ResponseEntity.ok(discussionThreadList);
    }

    @GetMapping(value = "/get-thread")
    public ResponseEntity<DiscussionReplyList> getThread(@RequestParam Integer id, Integer userId) {
        DiscussionThread discussionThread = discussionThreadService.getById(id);
        User user = userService.findById(discussionThread.getUserId());
        List<DiscussionReply> replyLists = discussionReplyService.list(new LambdaQueryWrapper<DiscussionReply>().eq(DiscussionReply::getThreadId, id));
        List<DiscussionReplyDTO> replyList = new ArrayList<>();
        for (var discussionReply : replyLists) {
            var discussionLikeReply = discussionLikeService.getOne(
                    new LambdaQueryWrapper<DiscussionLike>()
                            .eq(DiscussionLike::getReplyId, discussionReply.getId())
                            .eq(DiscussionLike::getUserId, userId));
            DiscussionReplyDTO discussionReplyDTO = new DiscussionReplyDTO(discussionReply.getId(),
                    discussionReply.getThreadId(),
                    discussionReply.getReplyId(),
                    discussionReply.getUserId(),
                    discussionReply.getContent(),
                    discussionReply.getLikes(),
                    discussionReply.getCreatedAt(),
                    discussionReply.getRepliedId(),
                    userService.findById(discussionReply.getUserId()).getName(),
                    userService.findById(discussionReply.getUserId()).getAvatar(),
                    discussionLikeReply != null);
            replyList.add(discussionReplyDTO);
            List<DiscussionReply> replyReplies = discussionReplyService.list(new LambdaQueryWrapper<DiscussionReply>().eq(DiscussionReply::getReplyId, discussionReply.getId()));
//            for (DiscussionReply discussionReplyReply : replyReplies) {
//                DiscussionLike discussionLikeReplyReply = discussionLikeService.getOne(new LambdaQueryWrapper<DiscussionLike>().eq(DiscussionLike::getReplyId, discussionReplyReply.getId()).eq(DiscussionLike::getUserId, userId));
//                DiscussionReplyDTO discussionReplyDTODTO = new DiscussionReplyDTO(discussionReplyReply.getId(),
//                        discussionReplyReply.getThreadId(),
//                        discussionReplyReply.getReplyId(),
//                        discussionReplyReply.getUserId(),
//                        discussionReplyReply.getContent(),
//                        discussionReplyReply.getLikes(),
//                        discussionReplyReply.getCreatedAt(),
//                        discussionReplyReply.getRepliedId(),
//                        userService.findById(discussionReplyReply.getUserId()).getName(),
//                        userService.findById(discussionReplyReply.getUserId()).getAvatar(),
//                        discussionLikeReplyReply != null);
//                replyList.add(discussionReplyDTODTO);
//            }
        }

        DiscussionLike discussionLike = discussionLikeService.getOne(new LambdaQueryWrapper<DiscussionLike>().eq(DiscussionLike::getThreadId, discussionThread.getId()).eq(DiscussionLike::getUserId, userId));
        DiscussionCollect discussionCollect = discussionCollectService.getOne(new LambdaQueryWrapper<DiscussionCollect>().eq(DiscussionCollect::getThreadId, discussionThread.getId()).eq(DiscussionCollect::getUserId, userId));
        DiscussionReplyList discussionReplyList = new DiscussionReplyList(discussionThread.getCourseId(),
                discussionThread.getUserId(),
                discussionThread.getTitle(),
                discussionThread.getContent(),
                discussionThread.getLikes(),
                discussionThread.getFavorites(),
                discussionThread.getClosed(),
                discussionThread.getTop(),
                discussionThread.getCreatedAt(),
                discussionThread.getTag(),
                discussionThread.getUpdatedAt(),
                replyList,
                user.getName(),
                user.getAvatar(),
                discussionLike != null,
                discussionCollect != null);

        return ResponseEntity.ok(discussionReplyList);
    }

    @PostMapping(value = "/change")
    public ResponseEntity<String> changeThread(@RequestBody DiscussionThread discussionThread) {
        discussionThreadService.updateById(discussionThread);

        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/thread-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> threadDelete(@RequestParam Integer id) {
        discussionThreadService.removeById(id);
        userService.deleteThreadNumber(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/reply-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> replyDelete(@RequestParam Integer id) {
        discussionReplyService.removeById(id);

        return ResponseEntity.noContent().build();
    }

    @Data
    @AllArgsConstructor
    public static class DiscussionThreadList {

        private List<DiscussionThreadDTO> discussionThreads;

    }

    @Data
    @AllArgsConstructor
    public static class DiscussionThreadDTO {

        private Integer id;

        private String courseId;

        private Integer userId;

        private String title;

        private String content;

        private Integer likes;

        private Integer favorites;

        private Integer closed;

        private Integer top;

        private String createdAt;

        private String tag;

        private String updatedAt;

        private boolean liked;

        private boolean isFavorite;

    }

    @Data
    @AllArgsConstructor
    public static class DiscussionReplyList {

        private String courseId;

        private Integer userId;

        private String title;

        private String content;

        private Integer likes;

        private Integer favorites;

        private Integer closed;

        private Integer top;

        private String createdAt;

        private String tag;

        private String updatedAt;

        private List<DiscussionReplyDTO> replyList;

        private String name;

        private String avatar;

        private boolean liked;

        private boolean isFavorite;

    }

    @Data
    @AllArgsConstructor
    public static class DiscussionReplyDTO {

        private Integer id;

        private Integer threadId;

        private Integer replyId;

        private Integer userId;

        private String content;

        private Integer likes;

        private String createdAt;

        private Integer repliedId;

        private String name;

        private String avatar;

        private boolean liked;

    }

}
