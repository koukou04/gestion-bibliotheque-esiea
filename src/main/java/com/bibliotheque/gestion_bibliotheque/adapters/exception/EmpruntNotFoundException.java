package com.bibliotheque.gestion_bibliotheque.adapters.exception;

public class EmpruntNotFoundException extends RuntimeException {
    public EmpruntNotFoundException(String message) {
        super(message);
    }

    public EmpruntNotFoundException(Long id) {
        super("Emprunt non trouvé avec l'ID: " + id);
    }
}
