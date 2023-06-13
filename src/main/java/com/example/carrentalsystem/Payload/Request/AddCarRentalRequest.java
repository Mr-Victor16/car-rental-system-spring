package com.example.carrentalsystem.Payload.Request;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCarRentalRequest {
    @NotNull
    private Long carID;

    @NotNull
    private Long userID;

    @NotNull
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate addDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;
}
