package com.example.carrentalsystem.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotNull
    private Long userID;

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 5, max = 120)
    private String newPassword;
}
