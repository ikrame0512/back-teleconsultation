package com.example.teleconsultation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrendreRdvDto {
    private Long patientId;
    private Long medecinId;
    private String dateDepart; // Le format "yyyy-MM-dd'T'HH:mm"
    private String dateFin;
}

