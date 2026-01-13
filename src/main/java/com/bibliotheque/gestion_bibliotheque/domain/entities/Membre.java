package com.bibliotheque.gestion_bibliotheque.domain.entities;

import java.time.LocalDate;

public class Membre {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String typeMembre; // ETUDIANT, ENSEIGNANT, PERSONNEL
    private Integer quotaEmprunt; // 5 pour étudiant, 10 pour enseignant, 7 pour personnel
    private Integer scoreFiabilite; // Score initial: 50
    private LocalDate dateInscription;

    // Constructeurs
    public Membre() {}

    // Constructeur simple pour inscrire un membre (quota sera calculé par le service)
    public Membre(Long id, String nom, String prenom, String email, String typeMembre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeMembre = typeMembre;
        this.dateInscription = LocalDate.now();
        this.scoreFiabilite = 50;
        this.quotaEmprunt = null; // Sera calculé par le service
    }

    // Constructeur complet
    public Membre(Long id, String nom, String prenom, String email, String typeMembre, Integer quotaEmprunt) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeMembre = typeMembre;
        this.dateInscription = LocalDate.now();
        this.scoreFiabilite = 50;
        this.quotaEmprunt = quotaEmprunt;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTypeMembre() { return typeMembre; }
    public void setTypeMembre(String typeMembre) {
        this.typeMembre = typeMembre;
    }

    public Integer getQuotaEmprunt() { return quotaEmprunt; }
    public void setQuotaEmprunt(Integer quotaEmprunt) {
        this.quotaEmprunt = quotaEmprunt;
    }

    public Integer getScoreFiabilite() { return scoreFiabilite; }
    public void setScoreFiabilite(Integer scoreFiabilite) {
        this.scoreFiabilite = scoreFiabilite;
    }

    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
}