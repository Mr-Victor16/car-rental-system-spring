package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.*;
import com.example.carrentalsystem.Payload.Request.*;
import com.example.carrentalsystem.Repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rental")
public class RentalController {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalStatusRepository rentalStatusRepository;
    private final StatusHistoryRepository statusHistoryRepository;

    public RentalController(CarRepository carRepository, UserRepository userRepository, RentalRepository rentalRepository,
                            RentalStatusRepository rentalStatusRepository,
                            StatusHistoryRepository statusHistoryRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.rentalStatusRepository = rentalStatusRepository;
        this.statusHistoryRepository = statusHistoryRepository;
    }

    @PostMapping("add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addRental(@RequestBody @Valid AddCarRentalRequest request){
        Car car = carRepository.getCarById(request.getCarID());

        StatusHistory statusHistory = new StatusHistory(
                rentalStatusRepository.findByName(ERentalStatus.STATUS_PENDING),
                request.getAddDate()
        );

        rentalRepository.save(new Rental(
                car,
                userRepository.getReferenceById(request.getUserID()),
                request.getStartDate(),
                request.getEndDate(),
                request.getAddDate(),
                Math.toIntExact((ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate())+1) * car.getPrice()),
                rentalStatusRepository.findByName(ERentalStatus.STATUS_PENDING),
                Collections.singletonList(statusHistoryRepository.save(statusHistory))
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
                RentalStatus rentalStatus = rentalStatusRepository.getReferenceById(statusID);
                rental.setRentalStatus(rentalStatus);

                StatusHistory newStatus = new StatusHistory(rentalStatus, LocalDate.now());
                rental.getStatusHistory().add(statusHistoryRepository.save(newStatus));
                rentalRepository.save(rental);

                if(rentalStatus.getName() == ERentalStatus.STATUS_ACCEPTED) {
                    if (rentalRepository.findByCarAndDateAndRentalStatus_Name(rental.getCar().getId(), rental.getStartDate(), rental.getEndDate(), ERentalStatus.STATUS_PENDING).size() != 0) {
                        List<Rental> rentalList = rentalRepository.findByCarAndDateAndRentalStatus_Name(rental.getCar().getId(), rental.getStartDate(), rental.getEndDate(), ERentalStatus.STATUS_PENDING);
                        for (Rental item : rentalList) {
                            if (!Objects.equals(item.getId(), id)) {
                                item.setRentalStatus(rentalStatusRepository.findByName(ERentalStatus.STATUS_REJECTED));
                                rentalRepository.save(item);
                            }
                        }
                    }
                }

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
            List<StatusHistory> historyList = new ArrayList<>(rentalRepository.getReferenceById(rentalID).getStatusHistory());
            for (StatusHistory item : historyList) statusHistoryRepository.deleteById(item.getId());

            rentalRepository.deleteById(rentalID);
            return new ResponseEntity<>("Rent removed successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("history/{rentalID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getRentalStatusHistory(@PathVariable("rentalID") Long rentalID){
        if(rentalRepository.existsById(rentalID)){
            return new ResponseEntity<>(rentalRepository.getReferenceById(rentalID).getStatusHistory(), HttpStatus.OK);
        }

        return new ResponseEntity<>("No rental found", HttpStatus.NOT_FOUND);
    }
}
