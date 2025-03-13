package com.example.teleconsultation.controllers;

import com.example.teleconsultation.dtos.GetAllRdvDto;
import com.example.teleconsultation.dtos.PrendreRdvDto;
import com.example.teleconsultation.models.RendezVous;
import com.example.teleconsultation.services.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @PostMapping("/prendre")
    public ResponseEntity<?> prendreRendezVous(@RequestBody PrendreRdvDto prendreRdvDto) {
        try {
            // Validation et traitement de les date, heures
            LocalDateTime dateDepart = LocalDateTime.parse(prendreRdvDto.getDateDepart());
            LocalDateTime dateFin = LocalDateTime.parse(prendreRdvDto.getDateFin());
            RendezVous rendezVous = rendezVousService.prendreRendezVous(
                    prendreRdvDto.getPatientId(),
                    prendreRdvDto.getMedecinId(),
                    dateDepart,
                    dateFin
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(rendezVous);
        } catch (Exception e) {
            // L'exception sera gérée par GlobalExceptionHandler
            throw new IllegalArgumentException("Erreur lors de la prise du rendez-vous : " + e.getMessage());
        }
    }

    @PostMapping("/accepter")
    public ResponseEntity<?> accepterRendezVous(@RequestParam Long rendezVousId) {
        try {
            RendezVous rendezVous = rendezVousService.accepterRendezVous(rendezVousId);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de l'acceptation du rendez-vous : " + e.getMessage());
        }
    }

    @PostMapping("/annuler")
    public ResponseEntity<?> annulerRendezVous(@RequestParam Long rendezVousId) {
        try {
            RendezVous rendezVous = rendezVousService.annulerRendezVous(rendezVousId);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de l'annulation du rendez-vous : " + e.getMessage());
        }
    }


    // Méthode pour récupérer les rendez-vous avec pagination
    @GetMapping("/getAllByMedecin")
    public ResponseEntity<Page<GetAllRdvDto>> getRendezVousByMedecin(
            @RequestParam Long medecinId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<GetAllRdvDto> rendezVousPage = rendezVousService.getRendezVousByMedecin(medecinId, page, size);
        return ResponseEntity.ok(rendezVousPage);
    }





//    @GetMapping("/getAllByMedecin")
//    public Page<GetAllRdvDto> getRendezVousByMedecin(@RequestParam Long medecinId,
//                                                     @RequestParam(defaultValue = "0") int page,
//                                                     @RequestParam(defaultValue = "10") int size) {
//        // Utiliser le medecinId pour filtrer les rendez-vous
//        Page<RendezVous> rendezVousPage = rendezVousService.getRendezVousByMedecin(medecinId, page, size);
//
//        // Convertir la page de RendezVous en page de GetAllRdvDto
//        Page<GetAllRdvDto> result = rendezVousPage.map(rdv -> {
//            GetAllRdvDto dto = new GetAllRdvDto();
//            dto.setId(rdv.getId());
//            dto.setDateDepart(rdv.getDateDepart());
//            dto.setHeureDepart(rdv.getHeureDepart());
//            dto.setDateFin(rdv.getDateFin());
//            dto.setHeureFin(rdv.getHeureFin());            dto.setStatut(rdv.getStatut());
//            dto.setPatientId(rdv.getPatient().getId());
//            dto.setPatientName(rdv.getPatient().getUtilisateur().getNom());
//            dto.setMedecinId(rdv.getMedecin().getId());
//            return dto;
//        });
//
//        return result;
//    }



}
