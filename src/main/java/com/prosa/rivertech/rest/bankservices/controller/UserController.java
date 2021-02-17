package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.config.SwaggerConfig;
import com.prosa.rivertech.rest.bankservices.dto.UserDto;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.mapper.UserMapper;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {SwaggerConfig.TAG_USER})
@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    @ApiOperation(value = "Retrieve all bank clients")
    public ResponseEntity<List<UserDto>> retrieveAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users.stream().map(user -> userMapper.mapToDto(user)).collect(Collectors.toList()));
    }

    @GetMapping("/users/{id}")
    @ApiOperation(value = "Retrieve client by id")
    public ResponseEntity<UserDto> retrieveUserById(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.mapToDto(userService.findById(id)));
    }

    @PostMapping("/users")
    @ApiOperation(value = "Create new client")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        User newUser = userService.save(userMapper.map(userDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/users")
    @ApiOperation(value = "Update client information")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.mapToDto(userService.update(userMapper.map(userDto))));
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation(value = "Delete client by ID")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deleted user id: " + id);
    }
}
