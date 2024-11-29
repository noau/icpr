package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Folder {

    @TableId
    private Integer id;

    private Integer userId;

    private String name;

    private String createdAt;

    private Integer isDefault;

    private Integer isPrivate;

    public Folder(Integer id, Integer userId, String name, String createdAt, Integer isDefault, Integer isPrivate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.createdAt = createdAt;
        this.isDefault = isDefault;
        this.isPrivate = isPrivate;
    }
}
