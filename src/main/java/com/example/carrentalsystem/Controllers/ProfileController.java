package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.User;
import com.example.carrentalsystem.Payload.Response.LoginResponse;
import com.example.carrentalsystem.Payload.Response.MessageResponse;
import com.example.carrentalsystem.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("my")
    public ResponseEntity<?> registerUser(@Valid @RequestHeader String token){
        if(userRepository.existsByToken(token)){
            User user = userRepository.getUserByToken(token);

            List<String> roles = user.getRoles().stream()
                    .map(item -> item.getName().name())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new LoginResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getToken(),
                    roles));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Bad token!"));
    }

}
