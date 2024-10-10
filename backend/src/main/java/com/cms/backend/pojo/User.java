package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * User 实体类，表示用户的数据模型
 */
@Getter
@Setter
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class User {

    /**
     * 用户ID，作为主键
     */
    @Id
    @Column(name = "id", nullable = false, unique = true, length = 255)
    private Integer id;

    /**
     * 用户姓名
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * 用户密码
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 用户班级
     */
    @Column(name = "class", nullable = false, length = 255)
    private String userClass;

    /**
     * 学院
     */
    @Column(name = "academy", nullable = false, length = 255)
    private String academy;

    /**
     * 用户性别
     */
    @Column(name = "gender", nullable = false, length = 255)
    private String gender;

    /**
     * 用户头像
     */
    @Column(name = "avatar", nullable = false, length = 255)
    private String avatar;

    /**
     * 关注数
     */
    @Column(name = "subscriptions_number", nullable = false, length = 255)
    private Integer subscriptionsNumber;

    /**
     * 粉丝数
     */
    @Column(name = "fans_number", nullable = false, length = 255)
    private Integer fansNumber;

}
