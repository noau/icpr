package com.cms.backend.controller;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.backend.pojo.*;
import com.cms.backend.pojo.DTO.*;
import com.cms.backend.service.*;
import com.cms.backend.utils.JWTUtils;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JavaMailSender mailSender;

    private final DiscussionThreadService discussionThreadService;

    private final FolderService folderService;

    private final FavoriteService favoriteService;

    private final NotificationService notificationService;

    private final DiscussionReplyService discussionReplyService;

    private final DiscussionLikeService discussionLikeService;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.nickname}")
    private String nickname;

    public UserController(DiscussionLikeService discussionLikeService, FavoriteService favoriteService, UserService userService, JavaMailSender mailSender, DiscussionThreadService discussionThreadService, FolderService folderService, NotificationService notificationService, DiscussionReplyService discussionReplyService) {
        this.discussionLikeService = discussionLikeService;
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.mailSender = mailSender;
        this.discussionThreadService = discussionThreadService;
        this.folderService = folderService;
        this.notificationService = notificationService;
        this.discussionReplyService = discussionReplyService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/login/student")
    public ResponseEntity<String> login(@RequestBody User user) {
        Integer id = user.getId();
        String password = user.getPassword();
        logger.info("Login attempt for account: {}", id);

        // 验证用户是否存在
        User userLogin = userService.findById(id);
        if (userLogin == null) {
            logger.warn("Login failed. User not found: {}", id);

            return ResponseEntity.status(410).body(""); // 用户不存在
        } else {
            // 验证密码是否正确
            if (password.equals(user.getPassword())) {
                if (userService.getTeacherInfo(id) == null) {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("account", id);
                    logger.info("User logged in successfully: {}", id);
                    var token = JWTUtils.genToken(claims);
                    logger.warn("User token: {}", token);

                    return ResponseEntity.ok(token);
                } else {

                    return ResponseEntity.status(410).body(""); // 用户身份不是学生
                }
            } else {
                logger.warn("Login failed. Incorrect password for account: {}", id);

                return ResponseEntity.status(422).body(""); // 密码错误
            }
        }
    }

    @PostMapping(value = "/login/teacher")
    public ResponseEntity<String> loginTeacher(@RequestBody User user) {
        Integer id = user.getId();
        String password = user.getPassword();

        // 验证用户是否存在
        User userLogin = userService.findById(id);
        if (userLogin == null) {

            return ResponseEntity.status(410).body(""); // 用户不存在
        } else {
            // 验证密码是否正确
            if (Objects.equals(password, user.getPassword())) {
                if (userService.getTeacherInfo(id) != null) {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("account", id);
                    var token = JWTUtils.genToken(claims);

                    return ResponseEntity.ok(token);
                } else {

                    return ResponseEntity.status(410).body(""); // 用户身份不是老师
                }
            } else {
                return ResponseEntity.status(422).body(""); // 密码错误
            }
        }
    }

    @PostMapping(value = "/login/administrator")
    public ResponseEntity<String> loginAdministrator(@RequestBody User user) {
        String password = user.getPassword();
        // 验证密钥是否正确
        if (Objects.equals(password, "icpr")) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("account", 123456789);
            var token = JWTUtils.genToken(claims);
            return ResponseEntity.ok(token);
        } else {

            return ResponseEntity.status(422).body(""); // 密码错误
        }
    }

    @GetMapping(value = "/info")
    public ResponseEntity<UserInfo> getUserInfo(@RequestParam Integer id) {
        User user = userService.findById(id);
        List<Folder> folderList = folderService.list(new LambdaQueryWrapper<Folder>().eq(Folder::getUserId, id));
        List<DiscussionThread> threadListList = discussionThreadService.list(new LambdaQueryWrapper<DiscussionThread>().eq(DiscussionThread::getUserId, id));
        List<DiscussionInfoDTO> threadList = new ArrayList<>();
        for (DiscussionThread discussionThread : threadListList) {
            User threadUser = userService.findById(discussionThread.getUserId());
            DiscussionLike discussionLike = discussionLikeService.getOne(new LambdaQueryWrapper<DiscussionLike>().eq(DiscussionLike::getThreadId, discussionThread.getId()).eq(DiscussionLike::getUserId, id));
            Favorite favorite = favoriteService.getOne(new LambdaQueryWrapper<Favorite>().eq(Favorite::getThreadId, discussionThread.getId()).eq(Favorite::getUserId, id));

            DiscussionInfoDTO discussionInfoDTO = new DiscussionInfoDTO(
                    discussionThread.getId(),
                    discussionThread.getCourseId(),
                    discussionThread.getUserId(),
                    discussionThread.getTitle(),
                    discussionThread.getContent(),
                    discussionThread.getLikes(),
                    discussionLike != null,
                    discussionThread.getFavorites(),
                    favorite != null,
                    discussionThread.getClosed(),
                    discussionThread.getTop(),
                    discussionThread.getCreatedAt(),
                    discussionThread.getTag(),
                    discussionThread.getUpdatedAt(),
                    threadUser.getName(),
                    user.getAvatar()
            );
            threadList.add(discussionInfoDTO);
        }

        UserInfo userInfo = new UserInfo(user.getId(), user.getName(), user.getUserClass(), user.getAcademy(), user.getGender(), user.getAvatar(), user.getSubscriptionsNumber(), user.getFansNumber(), user.getThreadNumber(), user.getEmail(), user.getPhoneNumber(), user.getIdCardNumber(), folderList, threadList);

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping(value = "/followers")
    public ResponseEntity<UserFollowDTO> getFollowers(@RequestParam Integer id) {
        List<FollowDTO> userFollowers = userService.getUserFollowers(id);
        UserFollowDTO userFollowDTO = new UserFollowDTO(userFollowers);

        return ResponseEntity.ok(userFollowDTO);
    }

    @GetMapping(value = "/subscriptions")
    public ResponseEntity<UserSubscriptionDTO> getSubscriptions(@RequestParam Integer id) {
        List<SubscriptionDTO> userSubscriptions = userService.getUserSubscriptions(id);
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO(userSubscriptions);

        return ResponseEntity.ok(userSubscriptionDTO);
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        Integer id = userChangePasswordDTO.getId();
        String password = userChangePasswordDTO.getPassword();
        String newPassword = userChangePasswordDTO.getNewPassword();
        User user = userService.findById(id);
        System.out.println(password);
        System.out.println(user.getPassword());

        // 验证密码是否正确
        if (Objects.equals(password, user.getPassword())) {
            userService.changePassword(id, newPassword);

            return ResponseEntity.ok("");
        } else {

            return ResponseEntity.status(422).body(""); // 密码错误
        }
    }

    @GetMapping(value = "/folders")
    public ResponseEntity<FolderDTO> getFolder(@RequestParam Integer id) {
        List<Folder> folders = userService.getUserFolders(id);
        FolderDTO folderDTO = new FolderDTO(folders);

        return ResponseEntity.ok(folderDTO);
    }

    @GetMapping(value = "/favorites")
    public ResponseEntity<FavoriteDTO> getFavorite(@RequestParam Integer id) {
        List<FavoriteGet> favorites = userService.getUserFavorites(id);
        FavoriteDTO favoritesDTO = new FavoriteDTO(favorites);

        return ResponseEntity.ok(favoritesDTO);
    }

    @PostMapping(value = "/change-info")
    public ResponseEntity<String> changeInfo(@RequestBody User user) {
        Integer id = user.getId();
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        userService.changeInfo(id, email, phoneNumber);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/create-folder")
    public ResponseEntity<String> createFolder(@RequestBody Folder folder) {
        Integer userId = folder.getUserId();
        String name = folder.getName();
        String createdAt = folder.getCreatedAt();
        Integer isPrivate = folder.getIsPrivate();
        userService.addFolder(userId, name, createdAt, 0, isPrivate);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/create-favorite")
    public ResponseEntity<String> createFavorite(@RequestBody Favorite favorites) {
        // 收藏之前需要校验是否已经收藏了
        Favorite favorite = favoriteService.getOne(new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, favorites.getUserId())
                .eq(Favorite::getThreadId, favorites.getThreadId()).eq(Favorite::getFolderId, favorites.getFolderId()));
        if (favorite != null) {
            return ResponseEntity.ok("帖子已被收藏");
        }
        Integer userId = favorites.getUserId();
        Integer threadId = favorites.getThreadId();
        Integer folderId = favorites.getFolderId();
        String createdAt = favorites.getCreatedAt();
        userService.addFavorite(userId, threadId, folderId, createdAt);
        userService.addFavorites(threadId);
//        userService.addThreadCollect(userId, threadId, discussionThreadService.getById(threadId).getCourseId(), createdAt);

        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/delete-folder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteFolder(@RequestBody Folder folder) {
        Integer id = folder.getId();
        userService.deleteFolder(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/change-folder")
    public ResponseEntity<String> changeFolder(@RequestBody Folder folder) {
        Integer id = folder.getId();
        String name = folder.getName();
        Integer isPrivate = folder.getIsPrivate();
        userService.changeFolder(id, name, isPrivate);

        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/delete-favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteFavorite(DeleteFavoriteCollect deleteFavoriteCollect) {
//        Integer id = deleteFavoriteCollect.getId();
//        Favorite favorite = userService.getFavorite(deleteFavoriteCollect.getId());
        userService.deleteFavorite(deleteFavoriteCollect.getId());
//        userService.deleteDiscussionCollect(favorite.getThreadId(), deleteFavoriteCollect.userId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/change-favorite")
    public ResponseEntity<String> changeFavorite(@RequestBody ChangeFavoriteDTO changeFavoriteDTO) {
        Integer id = changeFavoriteDTO.getId();
        Integer favoriteId = changeFavoriteDTO.getFavoriteId();
        userService.changeFavorite(id, favoriteId);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestBody User user) {
        Integer id = user.getId();
        String avatar = user.getAvatar();
        userService.uploadAvatar(id, avatar);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/make-subscription")
    public ResponseEntity<String> makeSubscription(@RequestBody Follow follow) {
        Integer followingId = follow.getFollowingId();
        String followingName = (userService.findById(followingId)).getName();
        Integer subscriptionId = follow.getSubscriptionId();
        String subscriptionName = (userService.findById(subscriptionId)).getName();
        try {
            userService.makeSubscription(followingId, subscriptionId, followingName, subscriptionName);
        } catch (Exception e) {
            return ResponseEntity.ok("");
        }
        userService.addFollower(followingId);
        userService.addSubscriber(subscriptionId);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        User user = userService.findById(follow.getFollowingId());
        Notification notification = new Notification(follow.getSubscriptionId(), "收到关注！", follow.getFollowingId(), "系统通知", 0, "你被" + user.getName() + "关注啦~", 0, formattedNow, "M310001B2计算机组成原理2024~2025上", 0);
        notificationService.save(notification);

        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/delete-subscription")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteSubscription(@RequestBody Follow follow) {
        Integer followingId = follow.getFollowingId();
        Integer subscriptionId = follow.getSubscriptionId();
        System.out.println(followingId);
        userService.deleteSubscription(followingId, subscriptionId);
        userService.deleteFollower(followingId);
        userService.deleteSubscriber(subscriptionId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/reset-pwd-email")
    public ResponseEntity<String> resetPwdEmail(@RequestBody User user) {
        Integer id = user.getId();
        String email = user.getEmail();
        String idCardNumber = user.getIdCardNumber();
        User checkUser = userService.findById(id);
        if (checkUser == null) {
            return ResponseEntity.status(410).body("");
        } else if (!Objects.equals(checkUser.getEmail(), email) && !Objects.equals(checkUser.getIdCardNumber(), idCardNumber)) {
            return ResponseEntity.status(422).body("邮箱和身份证均不匹配");
        } else if (Objects.equals(checkUser.getEmail(), email) && !Objects.equals(checkUser.getIdCardNumber(), idCardNumber)) {
            return ResponseEntity.status(422).body("身份证不匹配");
        } else if (!Objects.equals(checkUser.getEmail(), email) && Objects.equals(checkUser.getIdCardNumber(), idCardNumber)) {
            return ResponseEntity.status(422).body("邮箱不匹配");
        } else {
            userService.changePassword(id, String.valueOf(id));
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(nickname + '<' + sender + '>');
            message.setTo(email);
            message.setSubject("找回密码");
            String content = "您的密码已被重置为" + id + "请尽快修改新的密码";
            message.setText(content);
            mailSender.send(message);

            return ResponseEntity.ok("");
        }
    }

    public void sendSms(String phoneNumber, String signName, String templateCode, String templateParam) throws ExecutionException, InterruptedException {
        // 配置阿里云的认证信息
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId("") // AccessKey ID
                .accessKeySecret("") // AccessKey Secret
                .build());

        // 创建异步客户端
        AsyncClient client = AsyncClient.builder()
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();

        // 设置短信请求参数
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .phoneNumbers(phoneNumber) // 接收短信的手机号码
                .signName(signName) // 短信签名
                .templateCode(templateCode) // 短信模板代码
                .templateParam(templateParam) // 模板参数
                .build();

        // 发送短信
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));

        // 关闭客户端
        client.close();
    }

    @PostMapping(value = "/reset-pwd-phone")
    public ResponseEntity<String> resetPwdPhone(@RequestBody User user) {
        Integer id = user.getId();
        String phoneNumber = user.getPhoneNumber();
        String idCardNumber = user.getIdCardNumber();
        String regionPhoneNumber = "+86" + phoneNumber;
        User checkUser = userService.findById(id);
        if (checkUser == null) {
            return ResponseEntity.status(410).body("");
        } else if (!Objects.equals(checkUser.getPhoneNumber(), phoneNumber) && !Objects.equals(checkUser.getIdCardNumber(), idCardNumber)) {
            return ResponseEntity.status(422).body("手机号和身份证均不匹配");
        } else if (Objects.equals(checkUser.getPhoneNumber(), phoneNumber) && !Objects.equals(checkUser.getIdCardNumber(), idCardNumber)) {
            return ResponseEntity.status(422).body("身份证不匹配");
        } else if (!Objects.equals(checkUser.getPhoneNumber(), phoneNumber) && Objects.equals(checkUser.getIdCardNumber(), idCardNumber)) {
            return ResponseEntity.status(422).body("手机号不匹配");
        } else {
            try {
                userService.changePassword(id, String.valueOf(id));
                sendSms(regionPhoneNumber, "智慧课程平台", "SMS_474545302", String.format("{\"password\":\"%d\"}", id));

                return ResponseEntity.ok("");
            } catch (Exception e) {
                logger.error(e.getMessage());

                return ResponseEntity.status(500).body("短信发送失败!");
            }
        }
    }

    @PostMapping(value = "/discussion/like")
    public ResponseEntity<String> like(@RequestBody DiscussionLike discussionLike) {
        System.out.println(discussionLike.getCreatedAt());
        if (discussionLike.getThreadId() != 0) {
            userService.like(discussionLike.getUserId(), discussionLike.getThreadId(), discussionLike.getCourseId(), null, discussionLike.getCreatedAt());
            userService.addLikeThread(discussionLike.getThreadId());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);
            DiscussionThread discussionThread = discussionThreadService.getById(discussionLike.getThreadId());
            Notification notification = new Notification(discussionThread.getUserId(), "收到点赞！", discussionLike.getUserId(), "讨论区通知", discussionLike.getThreadId(), "你的帖子被赞啦~", 0, formattedNow, discussionLike.getCourseId(), 0);
            notificationService.save(notification);
        } else {
            userService.like(discussionLike.getUserId(), null, discussionLike.getCourseId(), discussionLike.getReplyId(), discussionLike.getCreatedAt());
            userService.addLikeReply(discussionLike.getReplyId());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);
            DiscussionReply discussionReply = discussionReplyService.getById(discussionLike.getReplyId());
            Notification notification = new Notification(discussionReply.getUserId(), "收到点赞！", discussionLike.getUserId(), "讨论区通知", discussionLike.getReplyId(), "你的评论被赞啦~", 0, formattedNow, discussionLike.getCourseId(), 0);
            notificationService.save(notification);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/discussion/delete-like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteLike(@RequestBody DiscussionLikeDTO discussionLikeDTO) {
        if (discussionLikeDTO.isThread == 1) {
            userService.deleteLikeThreadInfo(discussionLikeDTO.id, discussionLikeDTO.userId);
            userService.deleteLikeThread(discussionLikeDTO.id);
        } else {
            userService.deleteLikeReplyInfo(discussionLikeDTO.id, discussionLikeDTO.userId);
            userService.deleteLikeReply(discussionLikeDTO.id);
        }

        return ResponseEntity.noContent().build();
    }

    @Data
    @AllArgsConstructor
    public static class DeleteFavoriteCollect {

        private Integer id;

//        private Integer userId;

    }

    @Data
    @AllArgsConstructor
    public static class DiscussionLikeDTO {

        private Integer id;

        private Integer isThread;

        private Integer userId;

    }

    @Data
    @AllArgsConstructor
    public static class UserInfo {

        private Integer id;

        private String name;

        private String userClass;

        private String academy;

        private String gender;

        private String avatar;

        private Integer subscriptionsNumber;

        private Integer fansNumber;

        private Integer threadNumber;

        private String email;

        private String phoneNumber;

        private String idCardNumber;

        private List<Folder> folderList;

        private List<DiscussionInfoDTO> threadList;

    }

    @Data
    @AllArgsConstructor
    public static class DiscussionInfoDTO {

        private Integer id;

        private String courseId;

        private Integer userId;

        private String title;

        private String content;

        private Integer likes;

        private boolean liked;

        private Integer favorites;

        private boolean favorite;

        private Integer closed;

        private Integer top;

        private String createdAt;

        private String tag;

        private String updatedAt;

        private String userName;

        private String userAvatar;

    }

    @Data
    @AllArgsConstructor
    public static class FavoriteGet {

        private Integer id;

        private Integer userId;

        private Integer threadId;

        private Integer folderId;

        private String createdAt;

        private String title;

    }

}
