package com.bibliotheque.gestion_bibliotheque.adapters.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(Long id) {
        super("Réservation non trouvée avec l'ID: " + id);
    }
}
