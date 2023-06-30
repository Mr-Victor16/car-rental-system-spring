package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.models.Rental;
import com.example.carrentalsystem.services.RentalServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rentals")
public class RentalsController {
    private final RentalServiceImpl rentalService;

    public RentalsController(RentalServiceImpl rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRentals(){
        List<Rental> list = rentalService.findAll();
        System.out.println(list.get(0).getStatusHistory().get(0).getStatusAfterChange().getName());
        return ResponseEntity.ok(rentalService.findAll());
    }

    @GetMapping("{userID}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserRentals(@PathVariable("userID") Long userID){
        return ResponseEntity.ok(rentalService.findByUserId(userID));
    }
}
