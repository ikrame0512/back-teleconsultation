package com.example.teleconsultation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserRegistrationDto {
    private String nom;
    private String email;
    private String password;
    private String role;
}
