package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Role;
import com.example.carrentalsystem.models.RoleEnum;
import com.example.carrentalsystem.models.User;
import com.example.carrentalsystem.payload.request.AddUserRequest;
import com.example.carrentalsystem.payload.request.LoginRequest;
import com.example.carrentalsystem.payload.request.SignupRequest;
import com.example.carrentalsystem.payload.response.LoginResponse;
import com.example.carrentalsystem.repositories.RoleRepository;
import com.example.carrentalsystem.repositories.UserRepository;
import com.example.carrentalsystem.security.jwt.JWTUtils;
import com.example.carrentalsystem.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

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
    public void add(AddUserRequest addUserRequest) {
        User user = new User(
                addUserRequest.getUsername(),
                addUserRequest.getEmail(),
                encoder.encode(addUserRequest.getPassword()),
                findRole(addUserRequest.getRole())
        );

        userRepository.save(user);
    }

    @Override
    public void register(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), findRole("user"));
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

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long userID) {
        userRepository.deleteById(userID);
    }

    @Override
    public boolean verifyUserPassword(Long userID, String currentPassword) {
        return encoder.matches(currentPassword, getUserById(userID).getPassword());
    }

    @Override
    public void changeRole(Long userID, String role) {
        User user = getUserById(userID);
        user.setRole(findRole(role));
        userRepository.save(user);
    }

    @Override
    public Role findRole(String stringRole) {
        if(stringRole == null) {
            return roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            if(stringRole.equals("admin")) {
                return roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            } else {
                return roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }
        }
    }

}
