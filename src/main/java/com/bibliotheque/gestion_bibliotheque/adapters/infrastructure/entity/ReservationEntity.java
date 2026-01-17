package com.bibliotheque.gestion_bibliotheque.adapters.infrastructure.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "livre_id", nullable = false)
    private Long livreId;

    @Column(name = "membre_id", nullable = false)
    private Long membreId;

    @Column(name = "date_reservation", nullable = false)
    private LocalDate dateReservation;

    @Column(nullable = false)
    private String statut;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    // Constructeur vide
    public ReservationEntity() {}

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
