package com.example.learning.controller;

import com.example.learning.dto.AuthDTOs.*;
import com.example.learning.entity.Role;
import com.example.learning.entity.User;
import com.example.learning.security.JwtService;
import com.example.learning.service.AppUserDetailsService;
import com.example.learning.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtService jwtService;
    @Autowired private AppUserDetailsService userDetailsService;
    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails ud = userDetailsService.loadUserByUsername(req.username);
        Map<String,Object> claims = new HashMap<>();
        claims.put("roles", ud.getAuthorities());
        String token = jwtService.generateToken(ud.getUsername(), claims);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req){
        boolean canAssign = false;
        // If someone is logged in and has ADMIN role, allow role assignment
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            canAssign = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        } catch (Exception ignored){}
        User u = authService.register(req, canAssign);
        return ResponseEntity.ok("Registered as " + u.getUsername());
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(auth.getName());
    }
}
