package com.bibliotheque.gestion_bibliotheque.adapters.messaging.event;


import java.time.LocalDate;

public class RetourLivreEvent {
    private Long empruntId;
    private Long livreId;
    private Long membreId;
    private String titreLivre;
    private String nomMembre;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private boolean enRetard;
    private Double penalite;

    public RetourLivreEvent() {}

    public RetourLivreEvent(Long empruntId, Long livreId, Long membreId,
                            String titreLivre, String nomMembre,
                            LocalDate dateRetourPrevue, LocalDate dateRetourEffective,
                            boolean enRetard, Double penalite) {
        this.empruntId = empruntId;
        this.livreId = livreId;
        this.membreId = membreId;
        this.titreLivre = titreLivre;
        this.nomMembre = nomMembre;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.enRetard = enRetard;
        this.penalite = penalite;
    }

    // Getters et Setters
    public Long getEmpruntId() { return empruntId; }
    public void setEmpruntId(Long empruntId) { this.empruntId = empruntId; }

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public String getTitreLivre() { return titreLivre; }
    public void setTitreLivre(String titreLivre) { this.titreLivre = titreLivre; }

    public String getNomMembre() { return nomMembre; }
    public void setNomMembre(String nomMembre) { this.nomMembre = nomMembre; }

    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }

    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }

    public boolean isEnRetard() { return enRetard; }
    public void setEnRetard(boolean enRetard) { this.enRetard = enRetard; }

    public Double getPenalite() { return penalite; }
    public void setPenalite(Double penalite) { this.penalite = penalite; }
}