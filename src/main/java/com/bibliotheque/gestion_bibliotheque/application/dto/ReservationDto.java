package com.bibliotheque.gestion_bibliotheque.application.dto;

import java.time.LocalDate;

public class ReservationDto {
    private Long id;
    private Long livreId;
    private Long membreId;
    private LocalDate dateReservation;
    private String statut;
    private Integer position;
    private LocalDate dateExpiration;

    // Constructeur vide
    public ReservationDto() {}

    // Constructeur complet
    public ReservationDto(Long id, Long livreId, Long membreId, LocalDate dateReservation,
                          String statut, Integer position, LocalDate dateExpiration) {
        this.id = id;
        this.livreId = livreId;
        this.membreId = membreId;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.position = position;
        this.dateExpiration = dateExpiration;
    }

    // Getters et Setters
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
