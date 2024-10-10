package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSubscriptionDTO {

    private List<SubscriptionDTO> userSubscriptions;

    public UserSubscriptionDTO(List<SubscriptionDTO> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }

}
