package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.User;
import com.example.carrentalsystem.payload.request.LoginRequest;
import com.example.carrentalsystem.payload.request.SignupRequest;
import com.example.carrentalsystem.payload.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    LoginResponse authenticate(LoginRequest loginRequest);

    void add(SignupRequest signUpRequest);

    boolean existsById(Long userID);

    void changePassword(Long userID, String newPassword);

    User getUserById(Long userID);
}
