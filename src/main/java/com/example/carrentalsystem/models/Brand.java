package com.example.carrentalsystem.models;

import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "brands")
@NoArgsConstructor
@Getter
@Setter
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3)
    private String name;

    public Brand(String name){
        this.name = name;
    }
}
