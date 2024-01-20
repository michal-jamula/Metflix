package com.metflix.service;

import com.metflix.exceptions.UserNotFoundException;
import com.metflix.model.Authority;
import com.metflix.model.Enums.AuthoritiesEnum;
import com.metflix.model.User;
import com.metflix.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Captor
    ArgumentCaptor<User> userCaptor;

    User testUser = new User(1, "testName", "testSurname",
            "testUsername", LocalDate.now(),"123321",
            LocalDate.of(2002,2,15),
            new ArrayList<>(List.of(new Authority(1, AuthoritiesEnum.ROLE_MEMBER))),
            "password");


    @Test
    @DisplayName("find Paginated")
    void findPaginated() {
        //given
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        //when
        userService.findPaginated(1, 2, "id", "asc");
        userService.findPaginated(1, 2, "username", "desc");

        //then
        verify(userRepository, times(2)).findAll(captor.capture());
        assertThat(captor.getAllValues().get(0).getSort()).isEqualTo(Sort.by("id").ascending());
        assertThat(captor.getAllValues().get(1).getSort()).isEqualTo(Sort.by("username").descending());
    }

    @Test
    @DisplayName("Load User by username, user should exist")
    void loadUserByUsernameShouldPass() {
        //given
        User user = new User(testUser);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        //when
        var foundUser = userService.loadUserByUsername(user.getUsername());

        //then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Load User by username, user doesn't exist. Should throw Exception")
    void loadUserByUsernameShouldThrowException() {
        //given
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

        //when
        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(testUser.getUsername()));

        //then
        then(userRepository).should().findByUsername(anyString());
        then(userRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    @DisplayName("Validate user with normal values")
    void validateUser() {
        //given
        testUser.setPassword("pass");
        List<String> SUCCESS_LIST = List.of("success", "true");
        //UserService userServiceSpy = Mockito.spy(userService);


        //when
        List<String> expected = userService.validateUser(testUser, "pass");

        //then
        assertThat(expected).isEqualTo(SUCCESS_LIST);
    }

    @Test
    @DisplayName("Validate user with some nulls - should return errors")
    void validateUserWithNullsShouldReturnErrors() {
        //given
        User userWithNullUsername = new User(testUser);
        userWithNullUsername.setUsername(null);

        User userWithNullRegDate = new User(testUser);
        userWithNullRegDate.setUsername("validUsername");
        userWithNullRegDate.setRegistrationDate(null);

        User userWithFalsePassword = new User(testUser);
        userWithFalsePassword.setUsername("validUsername");
        userWithFalsePassword.setRegistrationDate(LocalDate.now());
        userWithFalsePassword.setPassword("wrongUserPassword");

        //when
        List<String> expected = userService.validateUser(userWithNullUsername, "password");
        List<String> expected2 = userService.validateUser(userWithNullRegDate, "password");
        List<String> expected3 = userService.validateUser(userWithFalsePassword, "password");

        //then
        assertThat(expected).isEqualTo(List.of("email", "Field is empty"));
        assertThat(expected2).isEqualTo(List.of("regDate", "Field is empty"));
        assertThat(expected3).isEqualTo(List.of("password", "Passwords do not match"));
    }

    @Test
    @DisplayName("emailExists - returns true if email exists in a database, otherwise false")
    void emailExistsUserDoesNotExist() {
        //given
        String existingEmail = "iExistInTheDatabase@gmail.com";
        String nonExistingEmail = "imNotInADatabase@gmail.com";
        given(userRepository.findByUsername(existingEmail)).willReturn(Optional.of(new User()));
        given(userRepository.findByUsername(nonExistingEmail)).willReturn(Optional.empty());


        //when
        boolean validEmail = userService.emailExists(existingEmail);
        boolean invalidEmail = userService.emailExists(nonExistingEmail);

        //then
        assertThat(validEmail).isTrue();
        assertThat(invalidEmail).isFalse();
    }

    @Test
    @DisplayName("checkPasswordsMatch - returns true if passwords match, otherwise false")
    void checkPasswordsMatch() {
        //given
        String pass1 = "aPassword";
        String pass2 = "notAPassword";

        //when
        boolean matchingPasswords = userService.checkPasswordsMatch(pass1, pass1);
        boolean notMatchingPasswords = userService.checkPasswordsMatch(pass1, pass2);

        //then
        assertThat(matchingPasswords).isTrue();
        assertThat(notMatchingPasswords).isFalse();
    }

    @Test
    @DisplayName("Save user - User has no ID or Authority, gives the user both and saves into repo")
    void saveNewUser() {
        //given
        User user = new User();
        user.setUsername("username");

        User userWithId = new User();
        userWithId.setId(1);
        userWithId.setUsername("username");
        userWithId.setRegistrationDate(LocalDate.now());

        User userWithIdAndAuthority = new User();
        userWithIdAndAuthority.setId(1);
        userWithIdAndAuthority.setUsername("username");
        userWithIdAndAuthority.setRegistrationDate(LocalDate.now());
        userWithIdAndAuthority.setAuthorities(List.of(new Authority(1, AuthoritiesEnum.ROLE_MEMBER)));

        given(userRepository.save(any())).willReturn(userWithId, userWithIdAndAuthority);

        //when
        User returnedUser = userService.save(user);

        //then
        assertThat(returnedUser.getId()).isEqualTo(1);
        assertThat(returnedUser.getRegistrationDate()).isNotNull();
        assertThat(returnedUser.getAuthorities()).isNotNull();

        //Cannot assert same hash since different users have been created to mock responses ?!?!?
        assertThat(returnedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("update User with normal values")
    void updateUserWithNormalValues() {

        //given
        User expectedUser = new User(testUser);
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setPassword(expectedUser.getPassword());

        given(userRepository.findById(any())).willReturn(Optional.of(currentUser));

        //when
        try {
            userService.updateUser(expectedUser);
        } catch (Exception e) {
            System.out.println("Exception was thrown during test");
        }

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        //then
        assertThat(updatedUser).isEqualTo(expectedUser);

        verify(userRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("update User with nulls - shouldn't update with nulls or empty strings")
    void updateUserExceptionalValues() {
        //given
        User expectedUser = new User(testUser);
        User emptyUser = new User();
        emptyUser.setId(1);
        emptyUser.setPassword(expectedUser.getPassword());

        given(userRepository.findById(1)).willReturn(Optional.of(expectedUser));

        //when
        try {
            userService.updateUser(emptyUser);
        } catch (Exception e) {
            System.out.println("Exception was thrown during test");
        }
        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        //then
        assertThat(updatedUser).isEqualTo(expectedUser);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Update user - matching authorities")
    void updateUserWithMatchingAuthorities() {
        //given
        User currentUser = new User();
        currentUser.setId(1);

        User updatingUser = new User();
        updatingUser.setId(1);

        given(userRepository.findById(1)).willReturn(Optional.of(currentUser));

        //when
        try {
            userService.updateUser(updatingUser);
        } catch (Exception e) {
            System.out.println("Exception was thrown during test");
        }
        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        //then
        assertThat(updatedUser).isEqualTo(currentUser);
        assertThat(updatedUser.getAuthorities()).isNotNull();

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Update User should throw exception if user doesn't exist")
    void updateUserShouldThrowException() {
        //given
        given(userRepository.findById(anyInt())).willReturn(Optional.empty());
        User user = new User();
        user.setId(1);

        User userWithNullId = new User();

        //when
        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(user));
        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(userWithNullId));

        verify(userRepository, times(1)).findById(anyInt());
        verifyNoMoreInteractions(userRepository);
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void findById() {
        //when
        userService.findById(1);

        //then
        verify(userRepository, times(1)).findById(1);
    }

}



