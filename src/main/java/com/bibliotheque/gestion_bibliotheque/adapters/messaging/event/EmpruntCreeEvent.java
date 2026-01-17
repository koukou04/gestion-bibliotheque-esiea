package com.bibliotheque.gestion_bibliotheque.adapters.messaging.event;

import java.time.LocalDateTime;

/**
 * Événement métier déclenché lors de la création d'un nouvel emprunt.
 * Cet événement est publié dans Kafka pour permettre des traitements asynchrones
 * (notifications, statistiques, etc.).
 */
public class EmpruntCreeEvent {
    private Long empruntId;
    private Long livreId;
    private String titreLivre;
    private Long membreId;
    private String nomMembre;
    private LocalDateTime dateEmprunt;
    private LocalDateTime dateRetourPrevu;
    private String eventType = "EMPRUNT_CREE";

    // Constructeur vide
    public EmpruntCreeEvent() {}

    // Constructeur complet
    public EmpruntCreeEvent(Long empruntId, Long livreId, String titreLivre,
                            Long membreId, String nomMembre, LocalDateTime dateEmprunt,
                            LocalDateTime dateRetourPrevu) {
        this.empruntId = empruntId;
        this.livreId = livreId;
        this.titreLivre = titreLivre;
        this.membreId = membreId;
        this.nomMembre = nomMembre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevu = dateRetourPrevu;
    }

    // Getters et Setters
    public Long getEmpruntId() {
        return empruntId;
    }

    public void setEmpruntId(Long empruntId) {
        this.empruntId = empruntId;
    }

    public Long getLivreId() {
        return livreId;
    }

    public void setLivreId(Long livreId) {
        this.livreId = livreId;
    }

    public String getTitreLivre() {
        return titreLivre;
    }

    public void setTitreLivre(String titreLivre) {
        this.titreLivre = titreLivre;
    }

    public Long getMembreId() {
        return membreId;
    }

    public void setMembreId(Long membreId) {
        this.membreId = membreId;
    }

    public String getNomMembre() {
        return nomMembre;
    }

    public void setNomMembre(String nomMembre) {
        this.nomMembre = nomMembre;
    }

    public LocalDateTime getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDateTime dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDateTime getDateRetourPrevu() {
        return dateRetourPrevu;
    }

    public void setDateRetourPrevu(LocalDateTime dateRetourPrevu) {
        this.dateRetourPrevu = dateRetourPrevu;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "EmpruntCreeEvent{" +
                "empruntId=" + empruntId +
                ", livreId=" + livreId +
                ", titreLivre='" + titreLivre + '\'' +
                ", membreId=" + membreId +
                ", nomMembre='" + nomMembre + '\'' +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevu=" + dateRetourPrevu +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
