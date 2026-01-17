package com.bibliotheque.gestion_bibliotheque.domain.entities;

import java.time.LocalDate;

public class Emprunt {
    private Long id;
    private Long livreId;
    private Long membreId;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private String statut; // EN_COURS, RETOURNE, RETOURNE_EN_RETARD
    private Double penalite;

    // Constructeurs
    public Emprunt() {}

    public Emprunt(Long id, Long livreId, Long membreId, LocalDate dateEmprunt,
                   LocalDate dateRetourPrevue) {
        this.id = id;
        this.livreId = livreId;
        this.membreId = membreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.statut = "EN_COURS";
        this.penalite = 0.0;
    }

    // ========== GETTERS ET SETTERS UNIQUEMENT ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Double getPenalite() { return penalite; }
    public void setPenalite(Double penalite) { this.penalite = penalite; }
}