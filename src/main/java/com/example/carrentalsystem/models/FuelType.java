package com.example.carrentalsystem.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fuel_types")
@NoArgsConstructor
@Getter
@Setter
public class FuelType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FuelTypeEnum name;

    public FuelType(FuelTypeEnum name){
        this.name = name;
    }
}
