package com.example.carrentalsystem.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@Getter
@Setter
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private CarModel model;

    private Integer year;

    private Integer mileage;

    @ManyToOne
    @JoinColumn(name = "fuel_type_id")
    private FuelType fuelType;

    private Integer horse_power;

    private String capacity;

    private Integer price;

    private boolean available;

    @OneToOne
    @JoinColumn(name = "car_image_image_id")
    private CarImage carImage;

    public Car(Brand brand, CarModel model, Integer year, Integer mileage, FuelType fuelType, Integer horse_power, String capacity, Integer price, boolean available, CarImage carImage) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.horse_power = horse_power;
        this.capacity = capacity;
        this.price = price;
        this.available = available;
        this.carImage = carImage;
    }
}
