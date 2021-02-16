package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.exception.BadRequestException;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.repository.UserRepository;
import com.prosa.rivertech.rest.bankservices.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        if (result.isPresent()) {
            user = result.get();
        } else {
            throw new NotFoundException("User id not found - " + id);
        }
        return user;
    }

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            throw new BadRequestException("User id mast be null");
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null || user.getId() <= 0) {
            throw new BadRequestException("Missing user id");
        }
        return this.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = this.findById(id);
        if (user != null) {
            userRepository.deleteById(id);
        }
    }
}
