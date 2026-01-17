package com.bibliotheque.gestion_bibliotheque.adapters.exception;

public class LivreNotFoundException extends RuntimeException {
    public LivreNotFoundException(String message) {
        super(message);
    }

    public LivreNotFoundException(Long id) {
        super("Livre non trouvé avec l'ID: " + id);
    }
}
