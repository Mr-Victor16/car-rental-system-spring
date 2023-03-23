package com.example.carrentalsystem.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "car_models")
@NoArgsConstructor
@Getter
@Setter
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public CarModel(String name){
        this.name = name;
    }
}
