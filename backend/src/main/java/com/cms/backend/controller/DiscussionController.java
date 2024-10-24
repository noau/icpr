package com.cms.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.DiscussionReply;
import com.cms.backend.pojo.DiscussionThread;
import com.cms.backend.service.DiscussionReplyService;
import com.cms.backend.service.DiscussionThreadService;
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

    public DiscussionController(DiscussionThreadService discussionThreadService, DiscussionReplyService discussionReplyService) {
        this.discussionThreadService = discussionThreadService;
        this.discussionReplyService = discussionReplyService;
    }

    @PostMapping(value = "/thread")
    public ResponseEntity<String> createThread(@RequestBody DiscussionThread discussionThread) {
        discussionThreadService.save(discussionThread);

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
        List<DiscussionReply> replyList = discussionReplyService.list(new LambdaQueryWrapper<DiscussionReply>().eq(DiscussionReply::getThreadId, id));
        List<DiscussionReply> replyReplyList = new ArrayList<>();
        for (DiscussionReply discussionReply : replyList) {
            List<DiscussionReply> replyReplies = discussionReplyService.list(new LambdaQueryWrapper<DiscussionReply>().eq(DiscussionReply::getReplyId, discussionReply.getId()));
            replyReplyList.addAll(replyReplies);
        }

        replyList.addAll(replyReplyList);
        DiscussionReplyList discussionReplyList = new DiscussionReplyList(discussionThread.getCourseId(), discussionThread.getUserId(), discussionThread.getTitle(), discussionThread.getContent(), discussionThread.getLikes(), discussionThread.getFavorites(), discussionThread.getClosed(), discussionThread.getTop(), discussionThread.getCreatedAt(), discussionThread.getTag(), discussionThread.getUpdatedAt(), replyList);

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

        private List<DiscussionReply> replyList;

    }

}
