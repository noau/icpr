package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Follow {

    @TableId
    private Integer subscriptionId;

    private Integer followingId;

    private String followingName;

    private String subscriptionName;

}
