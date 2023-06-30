package com.example.carrentalsystem.payload.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditCarRequest {
    @NotNull
    private Integer horsePower;

    @NotNull
    private Integer price;

    @NotNull
    private Integer year;

    @NotNull
    private Integer mileage;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String capacity;

    @NotNull
    private Long fuelType;
}
