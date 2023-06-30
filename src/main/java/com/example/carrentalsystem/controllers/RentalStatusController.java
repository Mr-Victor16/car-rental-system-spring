package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.services.RentalStatusServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rental-statuses")
public class RentalStatusController {
    private final RentalStatusServiceImpl rentalStatusService;

    public RentalStatusController(RentalStatusServiceImpl rentalStatusService) {
        this.rentalStatusService = rentalStatusService;
    }

    @GetMapping
    public ResponseEntity<?> getRentalStatusList(){
        return ResponseEntity.ok(rentalStatusService.findAll());
    }
}
