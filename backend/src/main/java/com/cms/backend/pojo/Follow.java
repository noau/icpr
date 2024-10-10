package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "follow", uniqueConstraints = {@UniqueConstraint(columnNames = "subscription_id")})
public class Follow {

    @Id
    @Column(name = "subscription_id", nullable = false, unique = true, length = 255)
    private Integer subscriptionId;

    @Column(name = "following_id", nullable = false, unique = true, length = 255)
    private Integer followingId;

    @Column(name = "following_name", nullable = false, unique = true, length = 255)
    private String followingName;

    @Column(name = "subscription_name", nullable = false, unique = true, length = 255)
    private String subscriptionName;

}
