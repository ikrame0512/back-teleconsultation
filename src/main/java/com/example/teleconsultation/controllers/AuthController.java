package com.example.teleconsultation.controllers;


import com.example.teleconsultation.dtos.UserLoginDto;
import com.example.teleconsultation.dtos.UserRegistrationDto;
import com.example.teleconsultation.enums.Role;
import com.example.teleconsultation.excepltions.UnauthorizedException;
import com.example.teleconsultation.models.Medecin;
import com.example.teleconsultation.models.Patient;
import com.example.teleconsultation.models.RendezVous;
import com.example.teleconsultation.models.Utilisateur;
import com.example.teleconsultation.repositories.MedecinRepository;
import com.example.teleconsultation.repositories.PatientRepository;
import com.example.teleconsultation.repositories.UtilisateurRepository;
import com.example.teleconsultation.security.JwtUtil;
import com.example.teleconsultation.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UtilisateurService utilisateurService;

    private final UtilisateurRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private MedecinRepository medecinRepository;
    @Autowired
    private PatientRepository patientRepository;

    public AuthController(UtilisateurService utilisateurService, UtilisateurRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
        Optional<Utilisateur> user = userRepository.findByEmail(userLoginDto.getEmail());
        Medecin medecin = medecinRepository.findByUtilisateurId(user.get().getId());
        Patient patient = patientRepository.findByUtilisateurId(user.get().getId());

        if (user.isPresent() && passwordEncoder.matches(userLoginDto.getPassword(), user.get().getPassword())) {
            if(user.get().getRole()== Role.MEDECIN){
                // Générez le token
                String token = jwtUtil.generateToken(userLoginDto.getEmail(), medecin.getId(),user.get().getRole().toString());
                // Réponse avec un objet structuré
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok().body(response);
            }else{
                // Générez le token
                String token = jwtUtil.generateToken(userLoginDto.getEmail(), patient.getId(),user.get().getRole().toString());
                // Réponse avec un objet structuré
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok().body(response);
            }


        }
        // Si les identifiants sont incorrects, on lance l'exception UnauthorizedException
        throw new UnauthorizedException("Identifiants incorrects");
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto userDto) {
        try {
            // Appel au service pour l'enregistrement de l'utilisateur
            utilisateurService.registerUser(userDto);
            // Réponse de succès
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Utilisateur créé avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);

        } catch (IllegalArgumentException e) {
            // L'exception IllegalArgumentException sera gérée par GlobalExceptionHandler
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Gestion d'autres exceptions, comme des erreurs internes
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Une erreur interne est survenue.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }




}
