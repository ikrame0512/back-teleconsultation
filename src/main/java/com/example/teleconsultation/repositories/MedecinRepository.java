package com.example.teleconsultation.repositories;

import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    Medecin findByUtilisateurId(Long utilisateurId);
    @Query("""
    SELECT d FROM Medecin d 
    WHERE d.id NOT IN (
        SELECT a.medecin.id FROM RendezVous a 
        WHERE (a.dateDepart < :dateFin AND a.dateFin > :dateDepart)
    )
""")
    List<Medecin> findAvailableDoctors(@Param("dateDepart") LocalDateTime dateDepart,
                                       @Param("dateFin") LocalDateTime dateFin);



}
