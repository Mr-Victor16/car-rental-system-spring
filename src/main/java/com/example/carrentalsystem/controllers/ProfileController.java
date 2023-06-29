package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.models.User;
import com.example.carrentalsystem.payload.request.ChangePasswordRequest;
import com.example.carrentalsystem.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public ProfileController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping("change-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request){
        if(userRepository.existsById(request.getUserID())){
            User user = userRepository.getReferenceById(request.getUserID());
            user.setPassword(encoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return new ResponseEntity<>("Password changed", HttpStatus.OK);
        }

        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

}
