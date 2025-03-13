package com.example.teleconsultation.services;

import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.repositories.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;


    // Méthode pour trouver les médecins disponibles
    public List<Medecin> findAvailableDoctors(LocalDateTime dateDepart, LocalDateTime dateFin) {
        return medecinRepository.findAvailableDoctors(dateDepart, dateFin);
    }
}
