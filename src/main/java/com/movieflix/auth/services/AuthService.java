package com.movieflix.auth.services;

import com.movieflix.auth.entities.User;
import com.movieflix.auth.entities.UserRoleEnum;
import com.movieflix.auth.repositories.UserRepository;
import com.movieflix.auth.dto.AuthResponse;
import com.movieflix.auth.dto.LoginRequest;
import com.movieflix.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRoleEnum.USER)
                .build();

        User savedUser = userRepository.save(user);
        var accessToken = jwtService.generateToken(savedUser);

        return "User Created!";
    }


    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        var accessToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
