package com.example.carrentalsystem.models;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@Getter
@Setter
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private CarModel model;

    @NotNull
    private Integer year;

    @NotNull
    private Integer mileage;

    @ManyToOne
    @JoinColumn(name = "fuel_type_id")
    private FuelType fuelType;

    @NotNull
    private Integer horsePower;

    @NotBlank
    private String capacity;

    @NotNull
    private Integer price;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "car_image_image_id")
    private CarImage carImage;

    public Car(Brand brand, CarModel model, Integer year, Integer mileage, FuelType fuelType, Integer horsePower, String capacity, Integer price, boolean available, CarImage carImage) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.horsePower = horsePower;
        this.capacity = capacity;
        this.price = price;
        this.available = available;
        this.carImage = carImage;
    }
}
