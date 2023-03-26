package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Payload.Response.MessageResponse;
import com.example.carrentalsystem.Repositories.FuelTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fuel")
public class FuelController {
    private final FuelTypeRepository fuelTypeRepository;

    public FuelController(FuelTypeRepository fuelTypeRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
    }

    @GetMapping("list")
    public ResponseEntity<?> getFuelList(){
        if(fuelTypeRepository.findAll().size() > 0){
            return ResponseEntity.ok(fuelTypeRepository.findAll());
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: No fuel found!"));
    }
}