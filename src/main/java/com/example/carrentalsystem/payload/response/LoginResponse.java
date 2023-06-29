package com.example.carrentalsystem.payload.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private Long id;
    private String username;
    private String email;
    private String token;
    private List<String> roles;
    private String type = "Bearer";

    public LoginResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
