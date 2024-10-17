package com.cms.backend.controller;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.cms.backend.pojo.DTO.*;
import com.cms.backend.pojo.Favorite;
import com.cms.backend.pojo.Folder;
import com.cms.backend.pojo.Follow;
import com.cms.backend.pojo.User;
import com.cms.backend.service.UserService;
import com.cms.backend.utils.JWTUtils;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.nickname}")
    private String nickname;

    public UserController(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Integer id = user.getId();
        String password = user.getPassword();
        logger.info("Login attempt for account: {}", id);

        // 验证用户是否存在
        User userLogin = userService.findByUserName(id);
        if (userLogin == null) {
            logger.warn("Login failed. User not found: {}", id);
            return ResponseEntity.status(410).body(""); // 用户不存在
        } else {

            // 验证密码是否正确
            if (Objects.equals(password, user.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("account", id);
                logger.info("User logged in successfully: {}", id);
                var token = JWTUtils.genToken(claims);
                logger.warn("User token: {}", token);
                return ResponseEntity.ok(token);
            } else {
                logger.warn("Login failed. Incorrect password for account: {}", id);
                return ResponseEntity.status(422).body(""); // 密码错误
            }
        }
    }

    @GetMapping(value = "/info")
    public ResponseEntity<User> getUserInfo(@RequestParam Integer id) {
        User user = userService.findByUserName(id);
        return ResponseEntity.ok(user);
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
        User user = userService.findByUserName(id);
        System.out.println(password);
        System.out.println(user.getPassword());

        // 验证密码是否正确
        if (Objects.equals(password, user.getPassword())) {
            userService.changePassword(id, newPassword);
            return ResponseEntity.ok("");
        } else {
            logger.warn("Login failed. Incorrect password for account: {}", id);
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
        List<Favorite> favorites = userService.getUserFavorites(id);
        FavoriteDTO favoritesDTO = new FavoriteDTO(favorites);
        return ResponseEntity.ok(favoritesDTO);
    }

    @PostMapping(value = "/change-info")
    public ResponseEntity<String> changeInfo(@RequestBody User user) {
        Integer id = user.getId();
        String name = user.getName();
        String userClass = user.getUserClass();
        String academy = user.getAcademy();
        String gender = user.getGender();
        userService.changeInfo(id, name, userClass, academy, gender);
        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/create-folder")
    public ResponseEntity<String> createFolder(@RequestBody Folder folders) {
        Integer userId = folders.getUserId();
        String name = folders.getName();
        String createdAt = folders.getCreatedAt();
        userService.addFolder(userId, name, createdAt, 0);
        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/create-favorite")
    public ResponseEntity<String> createFavorite(@RequestBody Favorite favorites) {
        Integer userId = favorites.getUserId();
        Integer threadId = favorites.getThreadId();
        Integer folderId = favorites.getFolderId();
        String createdAt = favorites.getCreatedAt();
        userService.addFavorite(userId, threadId, folderId, createdAt);
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
    public ResponseEntity<String> deleteFavorite(@RequestBody Favorite favorite) {
        Integer id = favorite.getId();
        userService.deleteFavorite(id);
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
        String followingName = (userService.findByUserName(followingId)).getName();
        Integer subscriptionId = follow.getSubscriptionId();
        String subscriptionName = (userService.findByUserName(subscriptionId)).getName();
        userService.makeSubscription(followingId, subscriptionId, followingName, subscriptionName);
        userService.addFollower(followingId);
        userService.addSubscriber(subscriptionId);
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
        User checkUser = userService.findByUserName(id);
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
        User checkUser = userService.findByUserName(id);
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
                sendSms(phoneNumber, "智慧课程平台", "SMS_474545302", String.format("{\"password\":\"%d\"}", id));

                return ResponseEntity.ok("");
            } catch (Exception e) {
                logger.error(e.getMessage());
                return ResponseEntity.status(500).body("短信发送失败!");
            }
        }
    }

}
