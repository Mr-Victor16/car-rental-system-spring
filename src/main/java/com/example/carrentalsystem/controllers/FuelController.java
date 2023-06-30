package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.services.FuelServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fuels")
public class FuelController {
    private final FuelServiceImpl fuelService;

    public FuelController(FuelServiceImpl fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping
    public ResponseEntity<?> getFuelList(){
        return ResponseEntity.ok(fuelService.findAll());
    }
}
