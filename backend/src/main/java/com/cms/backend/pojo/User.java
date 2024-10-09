package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * User 实体类，表示用户的数据模型
 */
@Setter
@Getter
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

    @Column(name = "class", nullable = false, length = 255)
    private String userClass;

    @Column(name = "gender", nullable = false, length = 255)
    private String gender;

    @Column(name = "avatar", nullable = false, length = 255)
    private String avatar;

    @Column(name = "default_folder_id", nullable = false, length = 255)
    private String defaultFolderID;

}
