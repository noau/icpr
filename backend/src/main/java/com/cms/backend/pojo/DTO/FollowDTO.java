package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDTO {

    private Integer followingId;

    private String followingName;

    public FollowDTO(Integer followingId, String followingName) {
        this.followingId = followingId;
        this.followingName = followingName;
    }

}
