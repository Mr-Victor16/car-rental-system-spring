package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.*;
import com.example.carrentalsystem.Payload.Request.*;
import com.example.carrentalsystem.Repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addRental(@RequestBody @Valid AddCarRentalRequest request){
        Car car = carRepository.getCarById(request.getCarID());

        rentalRepository.save(new Rental(
                car,
                userRepository.getReferenceById(request.getUserID()),
                request.getStartDate(),
                request.getEndDate(),
                request.getAddDate(),
                Math.toIntExact((ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate())+1) * car.getPrice()),
                rentalStatusRepository.findByName(ERentalStatus.STATUS_PENDING)
        ));

        return new ResponseEntity<>("Rental added", HttpStatus.OK);
    }

    @GetMapping("get/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRentals(){
        return ResponseEntity.ok(rentalRepository.findAll());
    }

    @GetMapping("get/user/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserRentals(@PathVariable("id") Long id){
        if(userRepository.existsById(id)){
            return ResponseEntity.ok(rentalRepository.findByUser_Id(id));
        } else {
            return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getRentalInfo(@PathVariable("id") Long id){
        if(rentalRepository.existsById(id)){
                return ResponseEntity.ok(rentalRepository.findById(id));
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("status/{statusID}/rental/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeStatus(@PathVariable("statusID") Long statusID, @PathVariable("id") Long id){
        if(rentalRepository.existsById(id)){
            if(rentalStatusRepository.existsById(statusID)){
                Rental rental = rentalRepository.getReferenceById(id);
                rental.setRentalStatus(rentalStatusRepository.getReferenceById(statusID));
                rentalRepository.save(rental);

                return new ResponseEntity<>("Rental status changed", HttpStatus.OK);
            }

            return new ResponseEntity<>("No rental status found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeRentalInformation(@RequestBody @Valid EditCarRentalRequest request){
        if(rentalRepository.existsById(request.getRentId())){
            Rental rental = rentalRepository.getReferenceById(request.getRentId());
            rental.setPrice(Math.toIntExact((ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate())+1) * rental.getCar().getPrice()));
            rental.setStartDate(request.getStartDate());
            rental.setEndDate(request.getEndDate());
            rentalRepository.save(rental);

            return new ResponseEntity<>("Rent details changed", HttpStatus.OK);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("delete/{rentalID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRental(@PathVariable("rentalID") Long rentalID){
        if(rentalRepository.existsById(rentalID)){
            rentalRepository.deleteById(rentalID);
            return new ResponseEntity<>("Rent removed successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }
}
