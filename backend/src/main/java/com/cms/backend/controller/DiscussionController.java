package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.DiscussionReply;
import com.cms.backend.pojo.DiscussionThread;
import com.cms.backend.pojo.User;
import com.cms.backend.service.DiscussionReplyService;
import com.cms.backend.service.DiscussionThreadService;
import com.cms.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    private final DiscussionThreadService discussionThreadService;

    private final DiscussionReplyService discussionReplyService;

    private final UserService userService;

    public DiscussionController(DiscussionThreadService discussionThreadService, DiscussionReplyService discussionReplyService, UserService userService) {
        this.discussionThreadService = discussionThreadService;
        this.discussionReplyService = discussionReplyService;
        this.userService = userService;
    }

    @PostMapping(value = "/thread")
    public ResponseEntity<String> createThread(@RequestBody DiscussionThread discussionThread) {
        discussionThreadService.save(discussionThread);
        userService.addThreadNumber(discussionThread.getUserId());

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/replies")
    public ResponseEntity<String> createReply(@RequestBody DiscussionReply discussionReply) {
        if (discussionReply.getThreadId() == 0) {
            discussionReplyService.createReply(discussionReply.getReplyId(), discussionReply.getUserId(), discussionReply.getContent(), discussionReply.getCreatedAt());
        } else {
            discussionReplyService.save(discussionReply);
        }

        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/course")
    public ResponseEntity<DiscussionThreadList> getCourse(@RequestParam String id) {
        List<DiscussionThread> discussionThreads = discussionThreadService.list(new LambdaQueryWrapper<DiscussionThread>().eq(DiscussionThread::getCourseId, id));
        DiscussionThreadList discussionThreadList = new DiscussionThreadList(discussionThreads);

        return ResponseEntity.ok(discussionThreadList);
    }

    @GetMapping(value = "/get-thread")
    public ResponseEntity<DiscussionReplyList> getThread(@RequestParam Integer id) {
        DiscussionThread discussionThread = discussionThreadService.getById(id);
        User user = userService.findByUserName(discussionThread.getUserId());
        List<DiscussionReply> replyLists = discussionReplyService.list(new LambdaQueryWrapper<DiscussionReply>().eq(DiscussionReply::getThreadId, id));
        List<DiscussionReplyDTO> replyList = new ArrayList<>();
        for (DiscussionReply discussionReply : replyLists) {
            DiscussionReplyDTO discussionReplyDTO = new DiscussionReplyDTO(discussionReply.getId(), discussionReply.getThreadId(), discussionReply.getReplyId(), discussionReply.getUserId(), discussionReply.getContent(), discussionReply.getLikes(), discussionReply.getCreatedAt(), userService.findByUserName(discussionReply.getUserId()).getName(), userService.findByUserName(discussionReply.getUserId()).getAvatar());
            replyList.add(discussionReplyDTO);
            List<DiscussionReply> replyReplies = discussionReplyService.list(new LambdaQueryWrapper<DiscussionReply>().eq(DiscussionReply::getReplyId, discussionReply.getId()));
            for (DiscussionReply discussionReplyReply : replyReplies) {
                DiscussionReplyDTO discussionReplyDTODTO = new DiscussionReplyDTO(discussionReplyReply.getId(), discussionReplyReply.getThreadId(), discussionReplyReply.getReplyId(), discussionReplyReply.getUserId(), discussionReplyReply.getContent(), discussionReplyReply.getLikes(), discussionReplyReply.getCreatedAt(), userService.findByUserName(discussionReplyReply.getUserId()).getName(), userService.findByUserName(discussionReplyReply.getUserId()).getAvatar());
                replyList.add(discussionReplyDTODTO);
            }

        }

        DiscussionReplyList discussionReplyList = new DiscussionReplyList(discussionThread.getCourseId(), discussionThread.getUserId(), discussionThread.getTitle(), discussionThread.getContent(), discussionThread.getLikes(), discussionThread.getFavorites(), discussionThread.getClosed(), discussionThread.getTop(), discussionThread.getCreatedAt(), discussionThread.getTag(), discussionThread.getUpdatedAt(), replyList, user.getName(), user.getAvatar());

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

        private List<DiscussionThread> discussionThreads;

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

        private String name;

        private String avatar;

    }

}
