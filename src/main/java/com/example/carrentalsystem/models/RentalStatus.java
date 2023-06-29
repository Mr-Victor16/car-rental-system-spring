package com.example.carrentalsystem.models;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "rental_statuses")
@NoArgsConstructor
@Getter
@Setter
public class RentalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RentalStatusEnum name;

    public RentalStatus(RentalStatusEnum name){
        this.name = name;
    }
}
