package com.metflix.service;

import com.metflix.model.ActiveSubscription;
import com.metflix.model.Authority;
import com.metflix.model.Enums.AuthoritiesEnum;
import com.metflix.model.User;
import com.metflix.repositories.SubscriptionRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@Tags({@Tag("service"), @Tag("test")})
@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    UserService userService;
    @Mock
    SubscriptionRepository subscriptionRepository;
    @InjectMocks
    SubscriptionService subscriptionService;

    @Test
    void subscribeUser() {
        //given
        User user = new User();
        user.setId(1);
        when(userService.findById(anyInt())).thenReturn(Optional.of(user));

        //when
        subscriptionService.subscribeUser(1, "subId");

        //then
        verify(userService, times(1)).updateUser(any());
        verify(subscriptionRepository, times(1)).save(any());


    }

    @Test
    void unsubscribeUser() {
        //given
        User subscribedUser = new User();
        subscribedUser.setId(1);
        subscribedUser.setAuthorities(new ArrayList<>(List.of(new Authority(2, AuthoritiesEnum.ROLE_SUBSCRIBED))));
        ActiveSubscription sub = new ActiveSubscription("test", 1);
        when(subscriptionRepository.findById("test")).thenReturn(Optional.of(sub));
        when(userService.findById(1)).thenReturn(Optional.of(subscribedUser));

        //when
        subscriptionService.unsubscribeUser("test");

        //then
        verify(userService, times(1)).updateUser(any());
        verify(subscriptionRepository, times(1)).deleteById("test");
        assert(subscribedUser.getAuthorities().isEmpty());

    }
}