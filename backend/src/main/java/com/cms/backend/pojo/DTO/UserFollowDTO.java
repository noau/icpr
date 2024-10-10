package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserFollowDTO {

    private List<FollowDTO> userFollowers;

    public UserFollowDTO(List<FollowDTO> userFollowers) {
        this.userFollowers = userFollowers;
    }

}
