package com.cashmobile.sellPhone.controller;

import com.cashmobile.sellPhone.entity.Admin;
import com.cashmobile.sellPhone.repository.AdminRepository;
import com.cashmobile.sellPhone.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String rawPassword = body.get("password");
        String hashedPassword = passwordEncoder.encode(rawPassword);

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(hashedPassword);
        adminRepo.save(admin);

        return "Admin registered!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin request) {
            Admin admin = adminRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(admin.getEmail());
        return ResponseEntity.ok().body(Collections.singletonMap("token", token));
    }
}