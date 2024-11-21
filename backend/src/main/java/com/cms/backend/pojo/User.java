package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * User 实体类，表示用户的数据模型
 */
@Data
public class User {

    /**
     * 用户ID，作为主键
     */
    @TableId
    private Integer id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户班级
     */
    private String userClass;

    /**
     * 学院
     */
    private String academy;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 关注数
     */
    private Integer subscriptionsNumber;

    /**
     * 粉丝数
     */
    private Integer fansNumber;

    /**
     * 帖子数
     */
    private Integer threadNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号
     */
    private String phoneNumber;

    /**
     * 身份证号
     */
    private String idCardNumber;

    /**
     * 互评指数
     */
    private Integer mark;

}
