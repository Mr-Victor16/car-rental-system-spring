package com.example.carrentalsystem.payload.response;

import com.example.carrentalsystem.models.RentalStatus;
import com.example.carrentalsystem.models.StatusHistory;
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
    private String username;
    private Long id;
    private Integer carPrice;
    private String carBrand;
    private String carModel;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate addDate;
    private Long price;
    private RentalStatus rentalStatus;
    private List<StatusHistory> statusHistory = new ArrayList<>();
}
