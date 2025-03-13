package com.example.teleconsultation.controllers;

import com.example.teleconsultation.models.RendezVous;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Méthode pour envoyer une notification lorsqu'un rendez-vous est pris
    @MessageMapping("/rendezvous/prendre")
    public void notifierMedecin(RendezVous rendezVous) {
        // Envoi de la notification au médecin
        messagingTemplate.convertAndSendToUser(
                String.valueOf(rendezVous.getMedecin().getUtilisateur().getEmail()),
                "/topic/rendezvous",
                "Nouveau rendez-vous pris par le patient: " + rendezVous.getPatient().getNom());
    }

    // Méthode pour envoyer une notification au patient lorsque le statut d'un rendez-vous change
    @MessageMapping({"/rendezvous/accepter","/rendezvous/annuler"})
    public void notifierPatient(RendezVous rendezVous) {
        // Envoi de la notification au patient
        messagingTemplate.convertAndSendToUser(
                String.valueOf(rendezVous.getPatient().getId()),
                "/topic/rendezvous",
                "Le statut de votre rendez-vous avec " + rendezVous.getMedecin().getNom() + " a changé: " + rendezVous.getStatut());
    }
}