package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Role;
import com.example.carrentalsystem.models.User;
import com.example.carrentalsystem.payload.request.AddUserRequest;
import com.example.carrentalsystem.payload.request.LoginRequest;
import com.example.carrentalsystem.payload.request.SignupRequest;
import com.example.carrentalsystem.payload.response.LoginResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    LoginResponse authenticate(LoginRequest loginRequest);

    void add(AddUserRequest signUpRequest);

    boolean existsById(Long userID);

    void changePassword(Long userID, String newPassword);

    User getUserById(Long userID);

    List<User> findAll();

    void delete(Long userID);

    void register(SignupRequest addUserRequest);

    boolean verifyUserPassword(Long userID, String currentPassword);

    void changeRole(Long userID, String role);

    Role findRole(String stringRole);
}
