package com.metflix.service;

import com.metflix.repositories.AuthorityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tags({@Tag("service"), @Tag("test")})
@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    @Mock
    AuthorityRepository authorityRepository;
    @InjectMocks
    AuthorityService authorityService;


    @Test
    @DisplayName("Find Paginated Authorities - normal test")
    void findPaginated() {
        //given
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        //when
        authorityService.findPaginated(1, 2, "id", "asc");
        authorityService.findPaginated(1, 2, "username", "desc");

        //then
        verify(authorityRepository, times(2)).findAll(captor.capture());
        assertThat(captor.getAllValues().get(0).getSort()).isEqualTo(Sort.by("id").ascending());
        assertThat(captor.getAllValues().get(1).getSort()).isEqualTo(Sort.by("username").descending());
    }
}