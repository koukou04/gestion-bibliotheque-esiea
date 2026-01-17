package com.bibliotheque.gestion_bibliotheque.domain.entities;

import java.time.LocalDate;

public class Reservation {
    private Long id;
    private Long livreId;
    private Long membreId;
    private LocalDate dateReservation;
    private String statut; // EN_ATTENTE, DISPONIBLE, ANNULEE, EXPIREE
    private Integer position;
    private LocalDate dateExpiration;

    // Constructeur vide
    public Reservation() {}

    // Constructeur avec paramètres
    public Reservation(Long id, Long livreId, Long membreId, LocalDate dateReservation, Integer position) {
        this.id = id;
        this.livreId = livreId;
        this.membreId = membreId;
        this.dateReservation = dateReservation;
        this.position = position;
        this.statut = "EN_ATTENTE";
        this.dateExpiration = null;
    }

    // ========== GETTERS ET SETTERS UNIQUEMENT ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public LocalDate getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
}