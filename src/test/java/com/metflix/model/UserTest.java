package com.metflix.model;

import com.metflix.model.Enums.AuthoritiesEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tags({@Tag("model"), @Tag("test")})
@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void addAuthority() {
        //given
        User userWithAuthorities = new User();
        User userWithoutAuthorities = new User();

        userWithAuthorities.setAuthorities(new ArrayList<>(List.of(new Authority(1, AuthoritiesEnum.ROLE_MEMBER))));

        //when
        userWithAuthorities.addAuthority(new Authority(2, AuthoritiesEnum.ROLE_SUBSCRIBED));
        userWithoutAuthorities.addAuthority(new Authority(2, AuthoritiesEnum.ROLE_SUBSCRIBED));

        //then
        assertThat(userWithAuthorities.getAuthorities()).isNotNull();
        assertThat(userWithoutAuthorities.getAuthorities()).isNotNull();

        assertThat(userWithAuthorities.getAuthorities().size()).isEqualTo(2);
        assertThat(userWithoutAuthorities.getAuthorities().size()).isEqualTo(1);


    }



}