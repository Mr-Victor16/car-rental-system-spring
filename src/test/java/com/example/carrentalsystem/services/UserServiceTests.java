package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Role;
import com.example.carrentalsystem.models.RoleEnum;
import com.example.carrentalsystem.models.User;
import com.example.carrentalsystem.payload.request.LoginRequest;
import com.example.carrentalsystem.payload.response.LoginResponse;
import com.example.carrentalsystem.repositories.RoleRepository;
import com.example.carrentalsystem.repositories.UserRepository;
import com.example.carrentalsystem.security.jwt.JWTUtils;
import com.example.carrentalsystem.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {
    private UserServiceImpl userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtils = mock(JWTUtils.class);
        authenticationManager = mock(AuthenticationManager.class);
        userService = new UserServiceImpl(userRepository, jwtUtils, authenticationManager, roleRepository, passwordEncoder);
    }

    //Role findRole(String stringRole);
    //Test for method when role name is null.
    @Test
    public void testFindRoleWithNullName(){
        Role userRole = new Role(RoleEnum.ROLE_USER);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));

        Role resultRole = userService.findRole(null);

        assertEquals(resultRole, userRole);
    }

    //Role findRole(String stringRole);
    //Method test when given an unknown role.
    @Test
    public void testFindRoleWithUnknownRoleName(){
        Role userRole = new Role(RoleEnum.ROLE_USER);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));

        Role resultRole = userService.findRole("moderator");

        assertEquals(resultRole, userRole);
    }

    //Role findRole(String stringRole);
    //Method test when given a known (admin) role.
    @Test
    public void testFindRoleWithAdminRole(){
        Role adminRole = new Role(RoleEnum.ROLE_ADMIN);
        when(roleRepository.findByName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        Role resultRole = userService.findRole("admin");

        assertEquals(resultRole, adminRole);
    }

    //Role findRole(String stringRole);
    //Method test when given a known (user) role.
    @Test
    public void testFindRoleWithUserRole(){
        Role userRole = new Role(RoleEnum.ROLE_USER);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));

        Role resultRole = userService.findRole("user");

        assertEquals(resultRole, userRole);
    }

    //void changeRole(Long userID, String role);
    //Test of a method that changes the user role from ADMIN to USER.
    @Test
    public void testChangeRoleToUser(){
        Long userID = 1L;

        Role oldRole = new Role(RoleEnum.ROLE_ADMIN);
        User existingUser = new User(userID,"username", "email@email.com", "password", oldRole, Collections.emptyList());
        when(userRepository.findById(userID)).thenReturn(Optional.of(existingUser));

        Role newRole = new Role(RoleEnum.ROLE_USER);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(newRole));

        when(userService.getUserById(userID)).thenReturn(existingUser);

        userService.changeRole(userID, "user");

        verify(userRepository, times(1)).getReferenceById(userID);
        verify(userRepository, times(1)).save(existingUser);
        assertEquals(newRole, existingUser.getRole());
    }

    //void changeRole(Long userID, String role);
    //Test of a method that changes the user role from USER to ADMIN.
    @Test
    public void testChangeRoleToAdmin(){
        Long userID = 1L;

        Role oldRole = new Role(RoleEnum.ROLE_USER);
        User existingUser = new User(userID,"username", "email@email.com", "password", oldRole, Collections.emptyList());
        when(userRepository.findById(userID)).thenReturn(Optional.of(existingUser));

        Role newRole = new Role(RoleEnum.ROLE_ADMIN);
        when(roleRepository.findByName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(newRole));

        when(userService.getUserById(userID)).thenReturn(existingUser);

        userService.changeRole(userID, "admin");

        verify(userRepository, times(1)).getReferenceById(userID);
        verify(userRepository, times(1)).save(existingUser);
        assertEquals(newRole, existingUser.getRole());
    }

    //boolean verifyUserPassword(Long userID, String currentPassword);
    //Method test when password is correct
    @Test
    public void testVerifyUserPasswordWithMatchPassword(){
        Long userID = 1L;
        String currentPassword = "currentPassword";
        String encodedPassword = "encodedPassword";

        User existingUser = new User("username", "email@email.com", encodedPassword);
        when(userRepository.getReferenceById(userID)).thenReturn(existingUser);
        when(passwordEncoder.matches(currentPassword, encodedPassword)).thenReturn(true);

        boolean result = userService.verifyUserPassword(userID, currentPassword);

        verify(userRepository, times(1)).getReferenceById(userID);
        verify(passwordEncoder, times(1)).matches(currentPassword, encodedPassword);
        assertTrue(result);
    }

    //boolean verifyUserPassword(Long userID, String currentPassword);
    //Method test when password is incorrect
    @Test
    public void testVerifyUserPasswordWithNotMatchPassword(){
        Long userID = 1L;
        String currentPassword = "currentPassword";
        String encodedPassword = "encodedPassword";

        User existingUser = new User("username", "email@email.com", encodedPassword);
        when(userRepository.getReferenceById(userID)).thenReturn(existingUser);
        when(passwordEncoder.matches(currentPassword, encodedPassword)).thenReturn(false);

        boolean result = userService.verifyUserPassword(userID, currentPassword);

        verify(userRepository, times(1)).getReferenceById(userID);
        verify(passwordEncoder, times(1)).matches(currentPassword, encodedPassword);
        assertFalse(result);
    }

    //void changePassword(Long userID, String newPassword);
    //Test of a method to change a user's password
    @Test
    public void testChangePassword(){
        Long userId = 1L;
        String newPassword = "newPassword";
        String encodedNewPassword = "encodedNewPassword";

        User existingUser = new User();
        when(userRepository.getReferenceById(userId)).thenReturn(existingUser);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        userService.changePassword(userId, newPassword);

        verify(userRepository, times(1)).getReferenceById(userId);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(existingUser);
        assertEquals(encodedNewPassword, existingUser.getPassword());
    }


    //LoginResponse authenticate(LoginRequest loginRequest);
    //Method test when the result isn't successful.
    @Test
    public void testAuthenticateWithFailedAuthentication() {
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(RuntimeException.class, () -> userService.authenticate(loginRequest));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, never()).generateJwtToken(any(Authentication.class));
    }

    //LoginResponse authenticate(LoginRequest loginRequest);
    //Method test when the result is successful.
    @Test
    public void testAuthenticateWithSuccessAuthentication() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        String token = "generatedJwtToken";

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        Role role = new Role(RoleEnum.ROLE_USER);
        User existingUser = new User(1L, "username", "user@email.com", "encodedPassword", role, Collections.emptyList());
        UserDetailsImpl userDetails = UserDetailsImpl.build(existingUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        LoginResponse loginResponse = userService.authenticate(loginRequest);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);

        assertEquals(token, loginResponse.getToken());
        assertEquals(userDetails.getId(), loginResponse.getId());
        assertEquals(userDetails.getUsername(), loginResponse.getUsername());
        assertEquals(userDetails.getEmail(), loginResponse.getEmail());
        assertEquals(Collections.singletonList("ROLE_USER"), loginResponse.getRoles());  // Assuming a default role
    }

}
