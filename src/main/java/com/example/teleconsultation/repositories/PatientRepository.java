package com.example.teleconsultation.repositories;

import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUtilisateurId(Long utilisateurId);

}
