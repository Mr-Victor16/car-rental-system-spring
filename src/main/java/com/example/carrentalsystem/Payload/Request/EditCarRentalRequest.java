package com.example.carrentalsystem.Payload.Request;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditCarRentalRequest {
    @NotNull
    private Long rentId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
