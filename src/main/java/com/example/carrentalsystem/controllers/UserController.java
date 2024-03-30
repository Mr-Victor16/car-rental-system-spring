package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.payload.request.AddUserRequest;
import com.example.carrentalsystem.payload.request.ChangePasswordRequest;
import com.example.carrentalsystem.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("{userID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("userID") Long userID){
        if(userID != 1) {
            if (userService.existsById(userID)) {
                userService.delete(userID);
                return new ResponseEntity<>("User removed successfully", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Default administrator account cannot be deleted", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody AddUserRequest request) {
        if(userService.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>("Username already in use", HttpStatus.CONFLICT);
        }

        if(userService.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email address already in use", HttpStatus.CONFLICT);
        }

        userService.add(request);
        return new ResponseEntity<>("User added", HttpStatus.OK);
    }

    @PutMapping("changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        if(userService.existsById(request.getUserID())){
            if(request.getNewPassword().equals(request.getOldPassword())){
                return new ResponseEntity<>("New and old passwords cannot be the same", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if(!userService.verifyUserPassword(request.getUserID(), request.getOldPassword())){
                return new ResponseEntity<>("Incorrect current password", HttpStatus.FORBIDDEN);
            }

            userService.changePassword(request.getUserID(), request.getNewPassword());
            return new ResponseEntity<>("User password changed successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("{userID}/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeRole(@PathVariable("userID") Long userID, @PathVariable("role") String role){
        if(userID != 1){
            if(userService.existsById(userID)){
                userService.changeRole(userID, role);
                return new ResponseEntity<>("User role changed successfully", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Role of the default administrator account cannot be changed", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
    }

}
