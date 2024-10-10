package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionDTO {

    private Integer subscriptionId;

    private String subscriptionName;

    public SubscriptionDTO(Integer subscriptionId, String subscriptionName) {
        this.subscriptionId = subscriptionId;
        this.subscriptionName = subscriptionName;
    }

}
