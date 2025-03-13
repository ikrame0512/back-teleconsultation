package com.example.teleconsultation.excepltions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Gérer toutes les exceptions génériques
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return new ResponseEntity<>("Erreur interne du serveur: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Gérer les exceptions spécifiques de type IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Gérer les erreurs d'authentification (par exemple, identifiants incorrects)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Gérer les erreurs liées à la parse de la date (ex: format incorrect)
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException e) {
        return new ResponseEntity<>("Format de date invalide : " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
