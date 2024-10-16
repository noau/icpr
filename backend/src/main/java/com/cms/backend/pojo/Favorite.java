package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorite", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Favorite {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 255)
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = true, length = 255)
    private Integer userId;

    @Column(name = "thread_id", nullable = false, unique = true, length = 255)
    private Integer threadId;

    @Column(name = "folder_id", nullable = false, unique = true, length = 255)
    private Integer folderId;

    @Column(name = "created_at", nullable = false, unique = true, length = 255)
    private String createdAt;

}
