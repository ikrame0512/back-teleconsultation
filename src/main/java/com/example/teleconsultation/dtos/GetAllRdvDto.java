package com.example.teleconsultation.dtos;

import com.example.teleconsultation.enums.StatutRendezVous;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data @Setter @Getter
public class GetAllRdvDto {
    private Long id;

    private String dateDepart;

    private String heureDepart;

    private String dateFin;

    private String heureFin;

    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut;

    private Long patientId;

    private String patientName;

    private Long medecinId;
}
