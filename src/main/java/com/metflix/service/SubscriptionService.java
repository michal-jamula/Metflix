package com.metflix.service;

import com.metflix.model.ActiveSubscription;
import com.metflix.model.Authority;
import com.metflix.model.Enums.AuthoritiesEnum;
import com.metflix.model.User;

import com.metflix.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(UserService userService, SubscriptionRepository subscriptionRepository) {
        this.userService = userService;
        this.subscriptionRepository = subscriptionRepository;
    }

    public void subscribeUser(int userId, String subscriptionId) {
        var userOptional = userService.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.addAuthority(new Authority(2, AuthoritiesEnum.ROLE_SUBSCRIBED));
            userService.updateUser(user);
            subscriptionRepository.save(new ActiveSubscription(subscriptionId, userId));
        }
    }

    public void unsubscribeUser(String subscriptionId) {
        var subscriptionOptional = subscriptionRepository.findById(subscriptionId);

        if (subscriptionOptional.isPresent()) {
            ActiveSubscription subscription = subscriptionOptional.get();

            var userOptional = userService.findById(subscription.getUserId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                var authorityToRemove = new Authority(2, AuthoritiesEnum.ROLE_SUBSCRIBED);
                user.removeAuthority(authorityToRemove);

                userService.updateUser(user);
                subscriptionRepository.deleteById(subscriptionId);
            } else {
                System.out.println("Active subscription's user is no longer registered");
            }
        } else {
            System.out.println("Tried to unsubscribe a bad subscription!");
        }
    }
}
