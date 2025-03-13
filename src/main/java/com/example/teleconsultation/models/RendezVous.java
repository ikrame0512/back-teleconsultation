package com.example.teleconsultation.models;

import com.example.teleconsultation.enums.StatutRendezVous;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateDepart;

    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut = StatutRendezVous.EN_ATTENTE;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Medecin medecin;
}