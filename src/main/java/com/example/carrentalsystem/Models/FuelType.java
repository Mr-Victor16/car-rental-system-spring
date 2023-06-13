package com.example.carrentalsystem.Models;

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
    private EFuelType name;

    public FuelType(EFuelType name){
        this.name = name;
    }
}
