package com.example.learning.service;

import com.example.learning.dto.AuthDTOs.RegisterRequest;
import com.example.learning.entity.Role;
import com.example.learning.entity.User;
import com.example.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public User register(RegisterRequest req, boolean canAssignRole) {
        if (userRepository.existsByUsername(req.username)) {
            throw new RuntimeException("Username already exists");
        }
        User u = new User();
        u.setUsername(req.username);
        u.setFullName(req.fullName);
        u.setPassword(encoder.encode(req.password));
        Set<Role> roles = new HashSet<>();
        if (canAssignRole && req.role != null) {
            roles.add(Role.valueOf(req.role.toUpperCase()));
        } else {
            roles.add(Role.STUDENT);
        }
        u.setRoles(roles);
        return userRepository.save(u);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
