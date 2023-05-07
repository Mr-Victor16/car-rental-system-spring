package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Repositories.RentalStatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rental/status")
public class RentalStatusController {
    private final RentalStatusRepository rentalStatusRepository;

    public RentalStatusController(RentalStatusRepository rentalStatusRepository) {
        this.rentalStatusRepository = rentalStatusRepository;
    }

    @GetMapping("list")
    public ResponseEntity<?> getRentalStatusList(){
        if(rentalStatusRepository.findAll().size() > 0){
            return ResponseEntity.ok(rentalStatusRepository.findAll());
        }

        return new ResponseEntity<>("No rental statuses found", HttpStatus.NOT_FOUND);
    }
}
