package com.example.carrentalsystem.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rental_statuses")
@NoArgsConstructor
@Getter
@Setter
public class RentalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERentalStatus name;

    public RentalStatus(ERentalStatus name){
        this.name = name;
    }
}