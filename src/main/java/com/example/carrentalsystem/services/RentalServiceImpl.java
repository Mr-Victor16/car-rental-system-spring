package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.*;
import com.example.carrentalsystem.payload.request.AddCarRentalRequest;
import com.example.carrentalsystem.payload.request.EditCarRentalRequest;
import com.example.carrentalsystem.payload.response.RentalResponse;
import com.example.carrentalsystem.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("rentalService")
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final RentalStatusRepository rentalStatusRepository;

    @Override
    public void changeStatus(Long statusID, Long rentalID) {
        Rental rental = rentalRepository.getReferenceById(rentalID);
        RentalStatus rentalStatus = rentalStatusRepository.findById(statusID).orElseThrow(() -> new RuntimeException("Error: Rental status is not found"));
        rental.setRentalStatus(rentalStatus);

        StatusHistory newStatus = new StatusHistory(rentalStatus, LocalDate.now());
        rental.getStatusHistory().add(addHistory(newStatus));
        rentalRepository.save(rental);
    }

    @Override
    public List<RentalResponse> findAll() {
        return userRepository.findAll().stream()
                .flatMap(user -> user.getRentals().stream()
                        .map(rental -> new RentalResponse(
                                user,
                                rental.getId(),
                                rental.getCar(),
                                rental.getStartDate(),
                                rental.getEndDate(),
                                rental.getAddDate(),
                                rental.getPrice(),
                                rental.getRentalStatus(),
                                rental.getStatusHistory().stream().toList()
                        )))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findByUserId(Long userID) {
        User user = userRepository.getReferenceById(userID);

        return user.getRentals().stream()
                .map(rental -> new Rental(
                        rental.getId(),
                        rental.getCar(),
                        rental.getStartDate(),
                        rental.getEndDate(),
                        rental.getAddDate(),
                        rental.getPrice(),
                        rental.getRentalStatus(),
                        rental.getStatusHistory().stream().toList()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long rentalID) {
        return rentalRepository.existsById(rentalID);
    }

    @Override
    public void update(Long rentalID, EditCarRentalRequest request) {
        Rental rental = rentalRepository.getReferenceById(rentalID);
        rental.setPrice((ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate())+1) * rental.getCar().getPrice());
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public void delete(Long rentalID) {
        rentalRepository.deleteById(rentalID);
    }

    @Override
    public Rental findById(Long rentalID) {
        return rentalRepository.getReferenceById(rentalID);
    }

    @Override
    public void add(AddCarRentalRequest request) {
        Car car = carRepository.getReferenceById(request.getCarID());
        StatusHistory statusHistory = new StatusHistory(rentalStatusRepository.findByName(RentalStatusEnum.STATUS_PENDING), request.getAddDate());

        User user = userRepository.getReferenceById(request.getUserID());
        user.getRentals().add(rentalRepository.save(
                    new Rental(
                            car,
                            request.getStartDate(),
                            request.getEndDate(),
                            request.getAddDate(),
                            (ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate())+1) * car.getPrice(),
                            rentalStatusRepository.findByName(RentalStatusEnum.STATUS_PENDING),
                            Collections.singletonList(addHistory(statusHistory))
                    )
                )
        );
        userRepository.save(user);
    }

    @Override
    public boolean existsDateAndStatus(LocalDate date, RentalStatusEnum status) {
        return rentalRepository.existsByRentalDateAndRentalStatus(date, status);
    }

    @Override
    public boolean existsByCarId(Long id) {
        return rentalRepository.existsByCarId(id);
    }

    private StatusHistory addHistory(StatusHistory status) {
        return statusHistoryRepository.save(status);
    }
}
