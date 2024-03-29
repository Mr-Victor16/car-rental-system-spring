package com.example.carrentalsystem.payload.response;

import com.example.carrentalsystem.models.Car;
import com.example.carrentalsystem.models.RentalStatus;
import com.example.carrentalsystem.models.StatusHistory;
import com.example.carrentalsystem.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RentalResponse {
    private User user;
    private Long id;
    private Car car;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate addDate;
    private Long price;
    private RentalStatus rentalStatus;
    private List<StatusHistory> statusHistory = new ArrayList<>();
}
