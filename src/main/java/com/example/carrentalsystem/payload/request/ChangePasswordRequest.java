package com.example.carrentalsystem.payload.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotNull
    private Long userID;

    @NotBlank
    private String newPassword;
}
