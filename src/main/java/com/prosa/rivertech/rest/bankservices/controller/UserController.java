package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> retrieveAllBeneficiaries(){
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUserById(@PathVariable long id){
        return userService.findById(id);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user){
        return userService.save(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User employee){
        userService.save(employee);
        return employee;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        User user = userService.findById(id);
        if(user == null){
            throw new RuntimeException("User is not found: "+id);
        }
        userService.delete(id);
        return "Deleted user id: "+id;
    }
}
