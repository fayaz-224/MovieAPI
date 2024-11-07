package com.movieflix.controllers;

import com.movieflix.entities.User;
import com.movieflix.repositories.UserRepository;
import com.movieflix.dto.LoginRequest;
import com.movieflix.dto.RegisterRequest;
import com.movieflix.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().body("User Created!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if(auth.isAuthenticated()) {
            //we can also use userDetailsService to load user
            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok().body(token);
        } else {
            throw new UsernameNotFoundException("Invalid credentials!");
        }
    }
}
