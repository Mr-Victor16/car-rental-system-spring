package com.example.carrentalsystem.models;

import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "car_models")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 30)
    private String name;

    public CarModel(String name){
        this.name = name;
    }
}
