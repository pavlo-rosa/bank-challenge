package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.dto.UserDto;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.mapper.UserMapper;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;


    @Test
    void retrieveAllUsers_Basic() throws Exception {
        when(userService.findAll()).thenReturn(
                Arrays.asList(
                        new User(1L, "Homer"),
                        new User(2L, "Bart"),
                        new User(3L, "Lisa")
                )
        );

        List<User> users = userService.findAll();
        ;

        when(userMapper.mapToDto(users.get(0))).thenReturn(
                new UserDto(1L, "Homer")
        );
        when(userMapper.mapToDto(users.get(1))).thenReturn(
                new UserDto(2L, "Bart")
        );
        when(userMapper.mapToDto(users.get(2))).thenReturn(
                new UserDto(3L, "Lisa")
        );

        RequestBuilder request = MockMvcRequestBuilders
                .get("/users")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{id:1,name:Homer},{id:2,name:Bart},{id:3,name:Lisa}]"))
                .andReturn();
    }

    @Test
    void retrieveUserById_Basic() throws Exception {
        Long idToFind = 1L;
        when(userService.findById(anyLong())).thenReturn(
                new User(1L, "Homer")
        );

        User mockUser = userService.findById(idToFind);
        when(userMapper.mapToDto(mockUser)).thenReturn(
                new UserDto(idToFind, "Homer")
        );


        RequestBuilder request = MockMvcRequestBuilders
                .get("/users/{userID}", idToFind)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{id:1,name:Homer}"))
                .andReturn();
    }

    @Test
    void addUser() throws Exception {
        User userMock = new User("Mr Burns");
        UserDto userDtoMock = new UserDto("Mr Burns");

        when(userMapper.map(userDtoMock)).thenReturn(new User("Mr Burns"));
        when(userService.save(userMock)).thenReturn(new User(1L, "Mr Burns"));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content("{name:\"Mr Burns\"}");

        MvcResult result = mockMvc.perform(request)
                .andReturn();
    }

    @Test
    void updateUser() throws Exception {
        User userMock = new User(1L, "Mr Burns");
        UserDto userDtoMock = new UserDto(1L, "Mr Burns");

        when(userMapper.map(userDtoMock)).thenReturn(new User(1L, "Mr Burns"));
        when(userService.save(userMock)).thenReturn(new User(1L, "Mr Burns"));
        RequestBuilder request = MockMvcRequestBuilders
                .put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content("{name:\"Mr Burns\"}");

        MvcResult result = mockMvc.perform(request)
                .andReturn();
    }

    @Test
    void deleteUser() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/users/userId}", 1L)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andReturn();
    }
}