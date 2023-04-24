package com.example.carrentalsystem.Payload.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCarRentalRequest {
    @NotNull
    private Integer carID;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate addDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    private String token;
}
