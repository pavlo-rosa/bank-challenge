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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        List<User> users = Arrays.asList( new User(1L, "Bart"),  new User(2L, "Lisa"));
        UserDto userDto1 = new UserDto(1L, "Bart");
        UserDto userDto2 = new UserDto(2L, "Lisa");

        when(userService.findAll()).thenReturn(users);
        when(userMapper.mapToDto(users.get(0))).thenReturn(userDto1);
        when(userMapper.mapToDto(users.get(1))).thenReturn(userDto2);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(Arrays.asList(userDto1, userDto2));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/users/", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    void retrieveUserById_Basic() throws Exception {
        User user = new User(1L, "Bart");
        UserDto userDto = new UserDto(1L, "Bart");

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.mapToDto(user)).thenReturn(userDto);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/users/{1}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    void addUser_Basic() throws Exception {

        when(userMapper.map(any(UserDto.class))).thenReturn(new User("Mr Burns"));
        when(userService.save(any(User.class))).thenReturn(new User(1L, "Mr Burns"));

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(new UserDto(null, "Mr. Burns"));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void updateUser_Basic() throws Exception {
        User user = new User(1L, "Bart");
        UserDto userDto = new UserDto(1L, "Bart");

        when(userMapper.map(any(UserDto.class))).thenReturn(user);
        when(userService.update(any(User.class))).thenReturn(user);
        when(userMapper.mapToDto(any(User.class))).thenReturn(userDto);

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/users")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonRequest));
    }

    @Test
    void deleteUser_Basic() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted user id: 1"));

        verify(userService, times(1)).delete(1L);
    }
}