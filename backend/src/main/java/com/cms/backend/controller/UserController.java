package com.cms.backend.controller;

import com.cms.backend.pojo.User;
import com.cms.backend.service.UserService;
import com.cms.backend.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        User user1 = userService.findByUserName(id);
        if (user1 == null) {
            logger.warn("Login failed. User not found: {}", id);
            return ResponseEntity.status(404).body(""); // 用户不存在
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
}
