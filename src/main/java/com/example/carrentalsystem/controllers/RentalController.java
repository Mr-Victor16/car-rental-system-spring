package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.payload.request.*;
import com.example.carrentalsystem.services.CarServiceImpl;
import com.example.carrentalsystem.services.RentalServiceImpl;
import com.example.carrentalsystem.services.RentalStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rental")
public class RentalController {
    private final RentalServiceImpl rentalService;
    private final CarServiceImpl carService;
    private final RentalStatusService rentalStatusService;

    public RentalController(RentalServiceImpl rentalService, CarServiceImpl carService, RentalStatusService rentalStatusService) {
        this.rentalService = rentalService;
        this.carService = carService;
        this.rentalStatusService = rentalStatusService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addRental(@RequestBody @Valid AddCarRentalRequest request){
        if(carService.existsById(request.getCarID())){
            rentalService.add(request);
            return new ResponseEntity<>("Rental added", HttpStatus.OK);
        }

        return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("{rentalID}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getRentalInfo(@PathVariable("rentalID") Long rentalID){
        return ResponseEntity.ok(rentalService.findById(rentalID));
    }

    @PutMapping("{rentalID}/status/{statusID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeStatus(@PathVariable("rentalID") Long rentalID, @PathVariable("statusID") Long statusID){
        if(rentalService.existsById(rentalID)){
            if(rentalStatusService.existsById(statusID)){
                rentalService.changeStatus(statusID, rentalID);
                return new ResponseEntity<>("Rental status changed", HttpStatus.OK);
            }

            return new ResponseEntity<>("No rental status found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("{rentalID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeRentalInformation(@PathVariable("rentalID") Long rentalID, @RequestBody @Valid EditCarRentalRequest request){
        if(rentalService.existsById(rentalID)){
            rentalService.update(rentalID, request);
            return new ResponseEntity<>("Rent details changed", HttpStatus.OK);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{rentalID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRental(@PathVariable("rentalID") Long rentalID){
        if(rentalService.existsById(rentalID)){
            rentalService.delete(rentalID);
            return new ResponseEntity<>("Rent removed successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }

}
