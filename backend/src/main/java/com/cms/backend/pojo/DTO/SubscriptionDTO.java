package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionDTO {

    private Integer subscriptionId;

    private String subscriptionName;

    private String userClass;

    private String academy;

    private String avatar;

    public SubscriptionDTO(Integer subscriptionId, String subscriptionName, String userClass, String academy, String avatar) {
        this.subscriptionId = subscriptionId;
        this.subscriptionName = subscriptionName;
        this.userClass = userClass;
        this.academy = academy;
        this.avatar = avatar;
    }

}
