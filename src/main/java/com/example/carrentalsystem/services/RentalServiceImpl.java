package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.*;
import com.example.carrentalsystem.payload.request.AddCarRentalRequest;
import com.example.carrentalsystem.payload.request.EditCarRentalRequest;
import com.example.carrentalsystem.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    @Override
    public List<Rental> findByUserId(Long userID) {
        return userRepository.getReferenceById(userID).getRentals();
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
    public void delete(Long rentalID) {
        deleteHistory(rentalID);
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
        user.getRentals().add(
                new Rental(
                        car,
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getAddDate(),
                        (ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate())+1) * car.getPrice(),
                        rentalStatusRepository.findByName(RentalStatusEnum.STATUS_PENDING),
                        Collections.singletonList(addHistory(statusHistory))
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

    private void deleteHistory(Long rentalID) {
        List<StatusHistory> historyList = new ArrayList<>(findById(rentalID).getStatusHistory());

        for (StatusHistory item : historyList) {
            statusHistoryRepository.deleteById(item.getId());
        }
    }

    private StatusHistory addHistory(StatusHistory status) {
        return statusHistoryRepository.save(status);
    }
}
