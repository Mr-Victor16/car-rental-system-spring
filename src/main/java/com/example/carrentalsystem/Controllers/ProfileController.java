package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.User;
import com.example.carrentalsystem.Payload.Request.ChangePasswordRequest;
import com.example.carrentalsystem.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest){
        if(userRepository.existsByToken(changePasswordRequest.getToken())) {
            User user = userRepository.getUserByToken(changePasswordRequest.getToken());
            user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);

            return new ResponseEntity<>("Password changed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Bad token", HttpStatus.FORBIDDEN);
        }
    }

}
