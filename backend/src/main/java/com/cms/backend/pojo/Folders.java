package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "folders", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Folders {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 255)
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = true, length = 255)
    private Integer userId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "created_at", nullable = false, length = 255)
    private String createdAt;

    @Column(name = "updated_at", nullable = false, length = 255)
    private String updatedAt;

    @Column(name = "is_default", nullable = false, length = 255)
    private Boolean isDefault;

}
