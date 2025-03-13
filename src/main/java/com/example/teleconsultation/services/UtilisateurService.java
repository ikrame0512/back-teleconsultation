package com.example.teleconsultation.services;

import com.example.teleconsultation.dtos.UserRegistrationDto;
import com.example.teleconsultation.dtos.UtilisateurDetailsDto;
import com.example.teleconsultation.enums.Role;
import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.models.Patient;
import com.example.teleconsultation.models.Utilisateur;
import com.example.teleconsultation.repositories.MedecinRepository;
import com.example.teleconsultation.repositories.PatientRepository;
import com.example.teleconsultation.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor @Data
@Service
@Slf4j
public class UtilisateurService {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private PatientRepository patientRepository;


    @Transactional
    public Utilisateur registerUser(UserRegistrationDto userDto) {
        //Afficher le contenu du userDto dans les logs
        log.info("Tentative de création d'un utilisateur avec les données : {}", userDto);

        // Vérification si l'email existe déjà dans la base de données
        if (utilisateurRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("L'email est déjà utilisé");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        if (userDto.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'utilisateur est obligatoire.");
        }

        Utilisateur newUser = new Utilisateur();
        newUser.setNom(userDto.getNom());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(Role.valueOf(userDto.getRole()));

        // Sauvegarder l'utilisateur avant de créer un médecin ou un patient
        Utilisateur savedUser = utilisateurRepository.save(newUser);

        // Créer un médecin ou un patient selon le rôle
        if (Role.MEDECIN == newUser.getRole()) {
            Medecin medecin = new Medecin();
            medecin.setUtilisateur(newUser);
            medecinRepository.save(medecin);
        } else if (Role.PATIENT == newUser.getRole()) {
            Patient patient = new Patient();
            patient.setUtilisateur(newUser);
            patientRepository.save(patient);
        }

        return savedUser;
    }


    // Méthode pour obtenir les détails de l'utilisateur en fonction de l'email
    public UtilisateurDetailsDto getUtilisateurDetailsByEmail(String email) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        // Si l'utilisateur existe
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            UtilisateurDetailsDto dto = new UtilisateurDetailsDto();
            // Set basic details
            dto.setNom(utilisateur.getEmail());  // Nous avons ici un exemple, vous pouvez ajouter plus d'infos.
            dto.setRole(String.valueOf(utilisateur.getRole()));

            // Vérification du rôle
            if (utilisateur.getRole().equals("MEDECIN") && utilisateur instanceof Medecin) {
                Medecin medecin = (Medecin) utilisateur;
                dto.setMedecinId(medecin.getId());
                dto.setSpecialite(medecin.getSpecialite());
            }

            if (utilisateur.getRole().equals("PATIENT") && utilisateur instanceof Patient) {
                Patient patient = (Patient) utilisateur;
                dto.setPatientId(patient.getId());
                dto.setAdresse(patient.getAdresse());
                dto.setNumero(patient.getNumero());
            }

            return dto;
        } else {
            return null; // L'utilisateur n'a pas été trouvé
        }
    }

}
