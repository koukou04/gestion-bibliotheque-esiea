package com.bibliotheque.gestion_bibliotheque.adapters.exception;

public class MembreNotFoundException extends RuntimeException {
    public MembreNotFoundException(String message) {
        super(message);
    }

    public MembreNotFoundException(Long id) {
        super("Membre non trouvé avec l'ID: " + id);
    }
}
