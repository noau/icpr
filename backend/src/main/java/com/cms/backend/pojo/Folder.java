package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Folders {

    @TableId
    private Integer id;


    private Integer userId;


    private String name;


    private String createdAt;


    private String updatedAt;


    private Boolean isDefault;

}
