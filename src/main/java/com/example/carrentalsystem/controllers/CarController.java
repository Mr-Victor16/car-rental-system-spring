package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.models.Car;
import com.example.carrentalsystem.models.RentalStatusEnum;
import com.example.carrentalsystem.payload.request.*;
import com.example.carrentalsystem.services.CarServiceImpl;
import com.example.carrentalsystem.services.RentalServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/car")
public class CarController {
    private final RentalServiceImpl rentalService;
    private final CarServiceImpl carService;

    public CarController(RentalServiceImpl rentalService, CarServiceImpl carService) {
        this.rentalService = rentalService;
        this.carService = carService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCar(@RequestBody @Valid AddCarRequest carRequest){
        carService.add(carRequest);
        return ResponseEntity.ok(carService.findAvailableCars());
    }

    @Transactional
    @PutMapping("{carID}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeCarImage(@PathVariable("carID") Long carID, @RequestBody MultipartFile file) throws IOException {
        if(carService.existsById(carID)){
            carService.changeImage(carID, file);
            return new ResponseEntity<>("Car photo changed", HttpStatus.OK);
        }

        return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("{carID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editCar(@PathVariable("carID") Long carID, @RequestBody @Valid EditCarRequest carRequest){
        if(carService.existsById(carID)){
            carService.update(carID, carRequest);
            return new ResponseEntity<>("Car information changed", HttpStatus.OK);
        }

        return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("{carID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCar(@PathVariable("carID") Long carID){
        return ResponseEntity.ok(carService.getCarById(carID));
    }

    @PutMapping("{carID}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeCarStatus(@PathVariable("carID") Long carID){
        if(carService.existsById(carID)){
            Car car = carService.getCarById(carID);

            if(!rentalService.existsDateAndStatus(LocalDate.now(), RentalStatusEnum.STATUS_ACCEPTED)){
                carService.changeStatus(car);
                return new ResponseEntity<>("The availability of the car has been changed", HttpStatus.OK);
            }

            return new ResponseEntity<>("Car has active rental", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{carID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCar(@PathVariable("carID") Long carID){
        if(carService.existsById(carID)){
            Car car = carService.getCarById(carID);

            if(!rentalService.existsByCarId(car.getId())){
                carService.delete(car);
                return new ResponseEntity<>("Car removed successfully", HttpStatus.OK);
            }

            return new ResponseEntity<>("You cannot remove a car if it has a rental assigned to it", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
    }

}
