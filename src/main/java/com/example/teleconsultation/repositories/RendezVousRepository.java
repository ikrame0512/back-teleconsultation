package com.example.teleconsultation.repositories;

import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.models.RendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long>, PagingAndSortingRepository<RendezVous, Long> {
    List<RendezVous> findByMedecinId(Long medecinId);
    List<RendezVous> findByPatientId(Long patientId);
    Page<RendezVous> findByMedecinId(Long medecinId, Pageable pageable);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin = :medecin " +
            "AND ((r.dateDepart BETWEEN :dateDepart AND :dateFin) " +
            "OR (r.dateFin BETWEEN :dateDepart AND :dateFin) " +
            "OR (r.dateDepart <= :dateDepart AND r.dateFin >= :dateFin))")
    List<RendezVous> findByMedecinAndDateRange(
            @Param("medecin") Medecin medecin,
            @Param("dateDepart") LocalDateTime dateDepart,
            @Param("dateFin") LocalDateTime dateFin);


}