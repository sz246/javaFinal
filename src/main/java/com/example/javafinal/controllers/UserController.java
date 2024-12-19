package com.example.javafinal.controllers;

import com.example.javafinal.models.User;
import com.example.javafinal.models.UserStatus;
import com.example.javafinal.repository.UserRepository;
import com.example.javafinal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user-control")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-control";
    }

    @GetMapping("/api/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/api/users")
    @ResponseBody
    public User addUserViaApi(@RequestBody User user) {
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User ID already exists");
        }
        return userRepository.save(user);
    }

    @PutMapping("/api/users/{id}")
    @ResponseBody
    public User updateUserViaApi(@PathVariable Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User ID does not exist");
        }
        user.setId(id);
        return userRepository.save(user);
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public String deleteUserViaApi(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User ID does not exist");
        }
        userRepository.deleteById(id);
        return "User with ID " + id + " has been deleted.";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("statuses", UserStatus.values());
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/user-control";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("statuses", UserStatus.values());
            return "add-user";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/user-control";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user", user);
        model.addAttribute("statuses", UserStatus.values());
        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id);
        userRepository.save(user);
        return "redirect:/user-control";
    }
}
