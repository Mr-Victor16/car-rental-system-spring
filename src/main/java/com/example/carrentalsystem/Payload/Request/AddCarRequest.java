package com.example.carrentalsystem.Payload.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCarRequest {
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

    @NotBlank
    private String token;
}
