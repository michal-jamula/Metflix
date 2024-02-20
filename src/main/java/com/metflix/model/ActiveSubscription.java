package com.metflix.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "active_subscriptions")
public class ActiveSubscription {

    @Id
    private String subscriptionId;
    private int userId;

    public ActiveSubscription(String subscriptionId, int userId) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
    }
}
