package com.example.carrentalsystem.Services;

import java.util.Random;

public class TokenService {

    public String generate(Long id){
        String alphanumeric = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder token = new StringBuilder(String.valueOf(id));
        Random random = new Random();

        for(int i=1; i<(10-token.toString().length()); i++){
            token.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }

        return token.toString();
    }
}
