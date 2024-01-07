package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Rental;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final RentalServiceImpl rentalService;

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
    public void add(AddUserRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        user.setRoles(setRole(signUpRequest.getRole()));
        userRepository.save(user);
    }

    @Override
    public void register(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
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
        List<Rental> userRentals = rentalService.findByUserId(userID);

        if(!userRentals.isEmpty()){
            for (Rental rentalItem : userRentals){
                rentalService.delete(rentalItem.getId());
            }
        }

        userRepository.deleteById(userID);
    }

    @Override
    public boolean verifyUserPassword(Long userID, String currentPassword) {
        return encoder.matches(currentPassword, getUserById(userID).getPassword());
    }

    @Override
    public void changeRole(Long userID, Boolean role) {
        User user = getUserById(userID);

        //Role: False - USER, True - ADMIN
        if(role) user.setRoles(setRole(new HashSet<>(List.of("admin"))));
        else user.setRoles(setRole(new HashSet<>(List.of("user"))));

        userRepository.save(user);
    }

    @Override
    public Set<Role> setRole(Set<String> stringRoles) {
        Set<Role> roles = new HashSet<>();

        if(stringRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            stringRoles.forEach(role -> {
                if(role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        return roles;
    }

}
