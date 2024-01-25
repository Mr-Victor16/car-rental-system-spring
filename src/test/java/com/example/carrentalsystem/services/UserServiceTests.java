package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Rental;
import com.example.carrentalsystem.models.Role;
import com.example.carrentalsystem.models.RoleEnum;
import com.example.carrentalsystem.models.User;
import com.example.carrentalsystem.payload.request.LoginRequest;
import com.example.carrentalsystem.payload.response.LoginResponse;
import com.example.carrentalsystem.repositories.RentalRepository;
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
    private RentalRepository rentalRepository;
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
        rentalRepository = mock(RentalRepository.class);
        userService = new UserServiceImpl(userRepository, jwtUtils, authenticationManager, roleRepository, passwordEncoder);
    }

    //Set<Role> setRole(Set<String> stringRoles);
    //Test method when instead of role names, there is null.
    @Test
    public void testSetRoleWithNullRoles(){
        Role userRole = new Role(RoleEnum.ROLE_USER);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));

        Role resultRole = userService.setRole(null);

        assertEquals(resultRole, userRole);
    }

    //Set<Role> setRole(Set<String> stringRoles);
    //Test whether the method correctly handles the absence of found roles in the repository.
    @Test
    public void testSetRoleWithMissingRoles(){
        when(roleRepository.findByName(any(RoleEnum.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.setRole(""));
    }

    //Set<Role> setRole(Set<String> stringRoles);
    //Method test when given an unknown role.
    @Test
    public void testSetRoleWithUnknownRole(){
        Role userRole = new Role(RoleEnum.ROLE_USER);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));

        Role resultRole = userService.setRole("moderator");

        assertEquals(resultRole, userRole);
    }

    //Set<Role> setRole(Set<String> stringRoles);
    //Method test when given a known (admin) role.
    @Test
    public void testSetRoleWithAdminRole(){
        Role adminRole = new Role(RoleEnum.ROLE_ADMIN);
        when(roleRepository.findByName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        Role resultRole = userService.setRole("admin");

        assertEquals(resultRole, adminRole);
    }

    //Set<Role> setRole(Set<String> stringRoles);
    //Method test when given a known (user) role.
    @Test
    public void testSetRoleWithUserRole(){
        Role userRole = new Role(RoleEnum.ROLE_USER);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));

        Role resultRole = userService.setRole("user");

        assertEquals(resultRole, userRole);
    }

    //void changeRole(Long userID, Boolean role);
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

        // False = USER
        userService.changeRole(userID, false);

        verify(userRepository, times(1)).getReferenceById(userID);
        verify(userRepository, times(1)).save(existingUser);
        assertEquals(newRole, existingUser.getRole());
    }

    //void changeRole(Long userID, Boolean role);
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

        // True = ADMIN
        userService.changeRole(userID, true);

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

    //void delete(Long userID);
    //Method test when user has rentals
    @Test
    public void testDeleteUserWithRentals() {
        Long userID = 1L;
        Rental rental1 = new Rental();
        rental1.setId(1L);
        Rental rental2 = new Rental();
        rental2.setId(2L);
        List<Rental> userRentals = Arrays.asList(rental1, rental2);

        //when(rentalRepository.findByUserId(userID)).thenReturn(userRentals);
        when(rentalRepository.getReferenceById(1L)).thenReturn(rental1);
        when(rentalRepository.getReferenceById(2L)).thenReturn(rental2);

        userService.delete(userID);

        //verify(rentalRepository, times(2)).deleteById(any());
        verify(userRepository, times(1)).deleteById(userID);
    }

    //void delete(Long userID);
    //Method test when user hasn't rentals
    @Test
    public void testDeleteUserWithoutRentals() {
        Long userID = 1L;
        List<Rental> userRentals = Collections.emptyList();

        //when(rentalRepository.findByUserId(userID)).thenReturn(userRentals);

        userService.delete(userID);

        verify(rentalRepository, times(0)).deleteById(any());
        verify(userRepository, times(1)).deleteById(userID);
    }
}
