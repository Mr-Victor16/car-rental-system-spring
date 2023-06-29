package com.example.carrentalsystem.models;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "car_models")
@NoArgsConstructor
@Getter
@Setter
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    public CarModel(String name){
        this.name = name;
    }
}
