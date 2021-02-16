package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.dto.UserDto;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.mapper.UserMapper;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> retrieveAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users.stream().map(user -> userMapper.mapToDto(user)).collect(Collectors.toList()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> retrieveUserById(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.mapToDto(userService.findById(id)));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.mapToDto(userService.save(userMapper.map(userDto))));
    }

    @PutMapping("/users")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.mapToDto(userService.update(userMapper.map(userDto))));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deleted user id: " + id);
    }
}
