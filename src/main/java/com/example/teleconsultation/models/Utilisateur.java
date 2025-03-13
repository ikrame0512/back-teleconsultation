package com.example.teleconsultation.models;

import com.example.teleconsultation.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Héritage pour Médecin et Patient
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // Médecin ou Patient

    @OneToOne(mappedBy = "utilisateur")
    @JsonIgnore
    private Medecin medecin;

    @OneToOne(mappedBy = "utilisateur")
    @JsonIgnore
    private Patient patient;
}

