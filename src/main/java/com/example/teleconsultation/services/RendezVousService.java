package com.example.teleconsultation.services;

import com.example.teleconsultation.controllers.NotificationController;
import com.example.teleconsultation.dtos.GetAllRdvDto;
import com.example.teleconsultation.enums.StatutRendezVous;
import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.models.Patient;
import com.example.teleconsultation.models.RendezVous;
import com.example.teleconsultation.repositories.MedecinRepository;
import com.example.teleconsultation.repositories.PatientRepository;
import com.example.teleconsultation.repositories.RendezVousRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RendezVousService {

    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private NotificationController notificationController;

    // Formatters pour formater la date et l'heure
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Méthode pour obtenir les rendez-vous avec pagination
    public Page<GetAllRdvDto> getRendezVousByMedecin(Long medecinId, int page, int size) {
        Page<RendezVous> rendezVousPage = rendezVousRepository.findByMedecinId(medecinId, PageRequest.of(page, size));

        // Mapper les entités RendezVous vers des DTO formatés
        return rendezVousPage.map(this::convertToDto);
    }

    private GetAllRdvDto convertToDto(RendezVous rendezVous) {
        GetAllRdvDto dto = new GetAllRdvDto();
        dto.setId(rendezVous.getId());

        // Formatage de la date et de l'heure
        dto.setDateDepart(formatDate(rendezVous.getDateDepart()));  // Format date (yyyy-MM-dd)
        dto.setHeureDepart(formatTime(rendezVous.getDateDepart())); // Extraire l'heure (HH:mm)

        dto.setDateFin(formatDate(rendezVous.getDateFin()));        // Format date (yyyy-MM-dd)
        dto.setHeureFin(formatTime(rendezVous.getDateFin()));       // Extraire l'heure (HH:mm)

        // Récupération des autres informations
        dto.setPatientId(rendezVous.getPatient().getId());
        dto.setPatientName(rendezVous.getPatient().getUtilisateur().getNom());
        dto.setStatut(rendezVous.getStatut());
        dto.setMedecinId(rendezVous.getMedecin().getId());

        return dto;
    }

    // Méthode pour formater la date (yyyy-MM-dd)
    private String formatDate(LocalDateTime date) {
        return date != null ? date.format(dateFormatter) : "";
    }

    // Méthode pour extraire l'heure (HH:mm)
    private String formatTime(LocalDateTime date) {
        return date != null ? date.format(timeFormatter) : "";
    }


    @Transactional
    public RendezVous prendreRendezVous(Long patientId, Long medecinId,
                                        LocalDateTime dateDepart,
                                        LocalDateTime dateFin) {
        Medecin medecin = medecinRepository.findById(medecinId).orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient non trouvé"));


        // Vérifier si le médecin est disponible (en fonction des rendez-vous existants)
        boolean isAvailable = checkAvailability(medecin, dateDepart, dateFin);
        if (!isAvailable) {
            throw new RuntimeException("Le médecin n'est pas disponible à cette période");
        }

        RendezVous rendezVous = new RendezVous();
        rendezVous.setPatient(patient);
        rendezVous.setMedecin(medecin);
        rendezVous.setDateDepart(dateDepart);
        rendezVous.setDateFin(dateFin);
        rendezVous.setStatut(StatutRendezVous.EN_ATTENTE);

        RendezVous savedRdv = rendezVousRepository.save(rendezVous);

        System.out.println("email : " + rendezVous.getMedecin().getUtilisateur().getEmail());
        System.out.println("context: " + SecurityContextHolder.getContext().getAuthentication().getName());

        messagingTemplate.convertAndSend(
                "/topic/rendezvous",
                "Nouveau rendez-vous pris par le patient: " + rendezVous.getPatient().getUtilisateur().getNom());

        // TODO :  not working because I'm getting anonymous user in spring security context even when adding the authenticated user to the context
        // Envoyer une notification au médecin
        //        notificationController.notifierMedecin(savedRdv);
        //        messagingTemplate.convertAndSendToUser(
        //               rendezVous.getMedecin().getUtilisateur().getEmail(),
        //                "/queue/rendezvous",
        //                "Nouveau rendez-vous pris par le patient: " + rendezVous.getPatient().getNom());
        return savedRdv;
    }


    public RendezVous accepterRendezVous(Long rendezVousId) {
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));
        rendezVous.setStatut(StatutRendezVous.CONFIRME);
        RendezVous updatedRdv = rendezVousRepository.save(rendezVous);

        // Envoyer une notification au patient
//        notificationController.notifierPatient(updatedRdv);

        messagingTemplate.convertAndSend(
                "/topic/rendezvous/accepter",
                "Le statut de votre rendez-vous avec " + rendezVous.getMedecin().getUtilisateur().getNom() + " a changé: " + rendezVous.getStatut());
        return updatedRdv;
    }

    public RendezVous annulerRendezVous(Long rendezVousId) {
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));
        rendezVous.setStatut(StatutRendezVous.ANNULE);
        RendezVous updatedRdv = rendezVousRepository.save(rendezVous);

        // Envoyer une notification au patient
//        notificationController.notifierPatient(updatedRdv);

        messagingTemplate.convertAndSend(
                "/topic/rendezvous/annuler",
                "Le statut de votre rendez-vous avec " + rendezVous.getMedecin().getUtilisateur().getNom() + " a changé: " + rendezVous.getStatut());
        return updatedRdv;
    }

    // Méthode pour vérifier la disponibilité du médecin
    private boolean checkAvailability(Medecin medecin, LocalDateTime dateDepart, LocalDateTime dateFin) {
        // Rechercher les rendez-vous du médecin dans la période demandée
        List<RendezVous> existingAppointments = rendezVousRepository.findByMedecinAndDateRange(
                medecin, dateDepart, dateFin);

        // Si un rendez-vous existe dans cette plage horaire, le médecin n'est pas disponible
        return existingAppointments.isEmpty();
    }

}
