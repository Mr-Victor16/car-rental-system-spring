package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.User;
import com.example.carrentalsystem.payload.request.LoginRequest;
import com.example.carrentalsystem.payload.request.SignupRequest;
import com.example.carrentalsystem.payload.response.LoginResponse;
import com.example.carrentalsystem.repositories.UserRepository;
import com.example.carrentalsystem.security.jwt.JWTUtils;
import com.example.carrentalsystem.security.services.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, JWTUtils jwtUtils, AuthenticationManager authenticationManager,
                           RoleServiceImpl roleService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LoginResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @Override
    public void add(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        user.setRoles(roleService.setRole(signUpRequest.getRole()));
        userRepository.save(user);
    }

    @Override
    public boolean existsById(Long userID) {
        return userRepository.existsById(userID);
    }

    @Override
    public void changePassword(Long userID, String newPassword) {
        User user = userRepository.getReferenceById(userID);
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long userID) {
        return userRepository.getReferenceById(userID);
    }
}
