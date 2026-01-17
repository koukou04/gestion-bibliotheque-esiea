package com.bibliotheque.gestion_bibliotheque.adapters.messaging.event;


import java.time.LocalDate;

public class ReservationCreeEvent {
    private Long reservationId;
    private Long livreId;
    private Long membreId;
    private String titreLivre;
    private String nomMembre;
    private String emailMembre;
    private LocalDate dateReservation;
    private Integer positionDansFile;

    public ReservationCreeEvent() {}

    public ReservationCreeEvent(Long reservationId, Long livreId, Long membreId,
                                String titreLivre, String nomMembre, String emailMembre,
                                LocalDate dateReservation, Integer positionDansFile) {
        this.reservationId = reservationId;
        this.livreId = livreId;
        this.membreId = membreId;
        this.titreLivre = titreLivre;
        this.nomMembre = nomMembre;
        this.emailMembre = emailMembre;
        this.dateReservation = dateReservation;
        this.positionDansFile = positionDansFile;
    }

    // Getters et Setters
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }

    public Long getMembreId() { return membreId; }
    public void setMembreId(Long membreId) { this.membreId = membreId; }

    public String getTitreLivre() { return titreLivre; }
    public void setTitreLivre(String titreLivre) { this.titreLivre = titreLivre; }

    public String getNomMembre() { return nomMembre; }
    public void setNomMembre(String nomMembre) { this.nomMembre = nomMembre; }

    public String getEmailMembre() { return emailMembre; }
    public void setEmailMembre(String emailMembre) { this.emailMembre = emailMembre; }

    public LocalDate getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDate dateReservation) { this.dateReservation = dateReservation; }

    public Integer getPositionDansFile() { return positionDansFile; }
    public void setPositionDansFile(Integer positionDansFile) { this.positionDansFile = positionDansFile; }
}