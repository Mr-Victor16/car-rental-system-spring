package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.Car;
import com.example.carrentalsystem.Models.ERentalStatus;
import com.example.carrentalsystem.Models.Rental;
import com.example.carrentalsystem.Payload.Request.AddCarRentalRequest;
import com.example.carrentalsystem.Payload.Response.MessageResponse;
import com.example.carrentalsystem.Repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.temporal.ChronoUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rental")
public class RentalController {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalStatusRepository rentalStatusRepository;

    public RentalController(CarRepository carRepository, UserRepository userRepository, RentalRepository rentalRepository,
                            RentalStatusRepository rentalStatusRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.rentalStatusRepository = rentalStatusRepository;
    }

    @PostMapping("add")
    public ResponseEntity<?> addCar(@Valid @RequestBody AddCarRentalRequest addCarRentalRequest){
        if(userRepository.existsByToken(addCarRentalRequest.getToken())) {
            Car car = carRepository.getCarById(addCarRentalRequest.getCarID());

            rentalRepository.save(new Rental(
                    car,
                    userRepository.getUserByToken(addCarRentalRequest.getToken()),
                    addCarRentalRequest.getStartDate(),
                    addCarRentalRequest.getEndDate(),
                    addCarRentalRequest.getAddDate(),
                    Math.toIntExact((ChronoUnit.DAYS.between(addCarRentalRequest.getStartDate(), addCarRentalRequest.getEndDate())+1) * car.getPrice()),
                    rentalStatusRepository.findByName(ERentalStatus.STATUS_PENDING)
            ));

            return ResponseEntity.ok(new MessageResponse("Success: Successfully added rental!"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Bad token!"));
        }
    }

}
