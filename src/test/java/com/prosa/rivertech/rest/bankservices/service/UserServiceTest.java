package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.exception.BadRequestException;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    @Test
    void findAll_Basic() {
        User user1 = new User(1L, "Homer");
        user1.setCreatedDate(new Date());
        user1.setUpdatedDate(new Date());

        User user2 = new User(1L, "Lisa");
        user1.setCreatedDate(new Date());
        user1.setUpdatedDate(new Date());

        when(userRepository.findAll()).thenReturn(Arrays.asList(
                user1,
                user2
        ));

        List<User> users = userService.findAll();

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0)).isEqualTo(user1);
        assertThat(users.get(1)).isEqualTo(user2);
    }

    @Test
    void findAll_Empty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());

        List<User> users = userService.findAll();

        assertThat(users).isNotNull().isEmpty();
    }

    @Test
    void findById_Basic() {
        Optional<User> mockOptionalUser = Optional.of(new User(1L, "Milhouse"));
        mockOptionalUser.get().setCreatedDate(new Date());
        mockOptionalUser.get().setUpdatedDate(new Date());

        when(userRepository.findById(1L)).thenReturn(mockOptionalUser);

        User user = userService.findById(1L);

        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(mockOptionalUser.get());
    }

    @Test
    void findById_NotFoundUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    userService.findById(1L);
                }).withMessage("User id not found - 1");
    }

    @Test
    void findById_NullIdParam() {

        when(userRepository.findById(null)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    userService.findById(null);
                }).withMessage("User id not found - null");
    }

    @Test
    void save_BasicIdAsZero() {
        User inputUser = new User(0L, "Bart");

        User expectedUser = new User(1L, "Bart");
        expectedUser.setUpdatedDate(new Date());
        expectedUser.setCreatedDate(new Date());

        when(userRepository.save(inputUser)).thenReturn(expectedUser);

        User createdUser = userService.save(inputUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser).isEqualTo(expectedUser);
    }

    @Test
    void save_BasicIdAsNull() {
        User inputUser = new User(null, "Bart");

        User expectedUser = new User(1L, "Bart");
        expectedUser.setUpdatedDate(new Date());
        expectedUser.setCreatedDate(new Date());

        when(userRepository.save(inputUser)).thenReturn(expectedUser);

        User createdUser = userService.save(inputUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser).isEqualTo(expectedUser);
    }

    @Test
    void save_BadRequestException() {
        User inputUser = new User(2L, "Bart");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> {
                    userService.save(inputUser);
                }).withMessage("User id must be null or zero");
    }

    @Test
    void update_Basic() {
        User inputUser = new User(3L, "The Barto");

        User expectedUser = new User(3L, "The Barto");
        expectedUser.setUpdatedDate(new Date());
        expectedUser.setCreatedDate(new Date());

        when(userRepository.save(inputUser)).thenReturn(expectedUser);

        User createdUser = userService.update(inputUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser).isEqualTo(expectedUser);
    }

    @Test
    void save_BadRequestExceptionByIdAsNull() {
        User inputUser = new User(null, "The Barto");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> {
                    userService.update(inputUser);
                }).withMessage("Missing user id");
    }

    @Test
    void save_BadRequestExceptionByIdAsZero() {
        User inputUser = new User(0L, "The Barto");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> {
                    userService.update(inputUser);
                }).withMessage("Missing user id");
    }

    @Test
    void delete_Basic() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "Marge")));
        userService.delete(1L);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void delete_UserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    userService.delete(1L);
                }).withMessage("User id not found - 1");
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).deleteById(anyLong());
    }
}