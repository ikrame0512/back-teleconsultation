package com.example.teleconsultation.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UtilisateurDetailsDto {
    private Long medecinId;
    private Long patientId;
    private String role;
    private String nom;
    private String specialite;  // Seulement pour les médecins
    private String adresse;     // Seulement pour les patients
    private String numero;
}
