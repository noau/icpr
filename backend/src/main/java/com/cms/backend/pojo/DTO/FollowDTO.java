package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDTO {

    private Integer followingId;

    private String followingName;

    private String userClass;

    private String academy;

    private String avatar;

    public FollowDTO(Integer followingId, String followingName, String userClass, String academy, String avatar) {
        this.followingId = followingId;
        this.followingName = followingName;
        this.userClass = userClass;
        this.academy = academy;
        this.avatar = avatar;
    }

}
