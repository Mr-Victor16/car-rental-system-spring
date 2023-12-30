package com.example.carrentalsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "status_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "status_after_change")
    private RentalStatus statusAfterChange;

    private LocalDate changeDate;

    public StatusHistory(RentalStatus statusAfterChange, LocalDate changeDate) {
        this.statusAfterChange = statusAfterChange;
        this.changeDate = changeDate;
    }
}
