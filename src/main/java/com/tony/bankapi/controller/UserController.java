package com.tony.bankapi.controller;

import com.tony.bankapi.dto.UserRequest;
import com.tony.bankapi.entity.User;
import com.tony.bankapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        
        User.Role role = User.Role.USER;
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            role = User.Role.valueOf(request.getRole().toUpperCase());
        }
        user.setRole(role);
        
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }
}
