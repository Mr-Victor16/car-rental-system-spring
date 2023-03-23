package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Repositories.CarRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("available")
    public ResponseEntity<?> registerUser(){
        return ResponseEntity.ok(carRepository.findByAvailable(true));
    }
}
