package com.example.teleconsultation.controllers;

import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.services.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/medecin")
public class MedecinController {

    @Autowired
    private MedecinService medecinService;



    // API pour obtenir les médecins disponibles
    @GetMapping("/disponibles")
    public List<Medecin> getAvailableDoctors(@RequestParam("dateDepart") String dateDepartStr,
                                             @RequestParam("dateFin") String dateFinStr) {
        // Convertir les chaînes de caractères en LocalDateTime
        LocalDateTime dateDepart = LocalDateTime.parse(dateDepartStr);
        LocalDateTime dateFin = LocalDateTime.parse(dateFinStr);

        // Appeler le service pour récupérer les médecins disponibles
        return medecinService.findAvailableDoctors(dateDepart, dateFin);
    }
}
