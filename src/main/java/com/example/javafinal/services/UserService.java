package com.example.javafinal.services;

import com.example.javafinal.models.User;
import com.example.javafinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User ID already exists");
        }
        userRepository.save(user);
    }
}
