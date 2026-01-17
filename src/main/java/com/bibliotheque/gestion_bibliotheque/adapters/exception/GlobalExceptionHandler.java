package com.bibliotheque.gestion_bibliotheque.adapters.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Handler global des exceptions de l'application.
 * Utilise @RestControllerAdvice pour intercepter toutes les exceptions
 * et renvoyer des réponses JSON structurées.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions de type "ressource non trouvée"
     */
    @ExceptionHandler({
            LivreNotFoundException.class,
            MembreNotFoundException.class,
            EmpruntNotFoundException.class,
            ReservationNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les exceptions métier (validation, règles métier)
     */
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            LivreIndisponibleException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessException(RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les autres exceptions non prévues
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Une erreur interne est survenue: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
