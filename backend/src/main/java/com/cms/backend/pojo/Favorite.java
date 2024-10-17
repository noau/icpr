package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Favorites {

    @TableId
    private Integer id;


    private Integer userId;


    private Integer threadId;


    private Integer folderId;


    private String createdAt;

}
