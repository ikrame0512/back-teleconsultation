package com.example.teleconsultation.controllers;

import com.example.teleconsultation.dtos.UtilisateurDetailsDto;
import com.example.teleconsultation.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/details")
    public UtilisateurDetailsDto getUtilisateurDetails(@RequestParam String email) {
        return utilisateurService.getUtilisateurDetailsByEmail(email);
    }
}