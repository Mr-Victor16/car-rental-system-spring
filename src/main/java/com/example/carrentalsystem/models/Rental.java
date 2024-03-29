package com.example.carrentalsystem.models;

import jakarta.validation.constraints.Min;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rentals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate addDate;

    @NotNull
    @Min(50)
    private Long price;

    @ManyToOne
    @JoinColumn(name = "rental_status_id")
    private RentalStatus rentalStatus;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "rental_id")
    private List<StatusHistory> statusHistory = new ArrayList<>();

    public Rental(Car car, LocalDate startDate, LocalDate endDate, LocalDate addDate, Long price, RentalStatus rentalStatus) {
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.addDate = addDate;
        this.price = price;
        this.rentalStatus = rentalStatus;
    }

    public Rental(Car car, LocalDate startDate, LocalDate endDate, LocalDate addDate, Long price, RentalStatus rentalStatus, List<StatusHistory> statusHistory) {
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.addDate = addDate;
        this.price = price;
        this.rentalStatus = rentalStatus;
        this.statusHistory = statusHistory;
    }
}
