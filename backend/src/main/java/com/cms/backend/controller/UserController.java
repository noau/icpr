package com.cms.backend.controller;

import com.cms.backend.pojo.DTO.*;
import com.cms.backend.pojo.Favorites;
import com.cms.backend.pojo.Folders;
import com.cms.backend.pojo.Follow;
import com.cms.backend.pojo.User;
import com.cms.backend.service.UserService;
import com.cms.backend.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping(value = "/change_password")
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
    public ResponseEntity<FolderDTO> getFolders(@RequestParam Integer id) {
        List<Folders> folders = userService.getUserFolders(id);
        FolderDTO folderDTO = new FolderDTO(folders);
        return ResponseEntity.ok(folderDTO);
    }

    @GetMapping(value = "/favorites")
    public ResponseEntity<FavoritesDTO> getFavorites(@RequestParam Integer id) {
        List<Favorites> favorites = userService.getUserFavorites(id);
        FavoritesDTO favoritesDTO = new FavoritesDTO(favorites);
        return ResponseEntity.ok(favoritesDTO);
    }

    @PostMapping(value = "/change_info")
    public ResponseEntity<String> changeInfo(@RequestBody User user) {
        Integer id = user.getId();
        String name = user.getName();
        String userClass = user.getUserClass();
        String academy = user.getAcademy();
        String gender = user.getGender();
        userService.changeInfo(id, name, userClass, academy, gender);
        return ResponseEntity.ok("");
    }

}
