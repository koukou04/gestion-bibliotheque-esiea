package com.bibliotheque.gestion_bibliotheque.domain.entities;

import java.time.LocalDate;

public class Membre {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String typeMembre; // ETUDIANT, ENSEIGNANT, PERSONNEL
    private Integer quotaEmprunt;
    private Integer scoreFiabilite;
    private LocalDate dateInscription;

    // Constructeurs
    public Membre() {}

    public Membre(Long id, String nom, String prenom, String email, String typeMembre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeMembre = typeMembre;
        this.dateInscription = LocalDate.now();
        this.scoreFiabilite = 50;
        // ✅ On initialise le quota dans le constructeur c'est OK
        this.quotaEmprunt = calculerQuotaInitial(typeMembre);
    }

    // ✅ Méthode privée helper pour l'initialisation c'est OK
    private Integer calculerQuotaInitial(String type) {
        switch (type.toUpperCase()) {
            case "ETUDIANT": return 5;
            case "ENSEIGNANT": return 10;
            case "PERSONNEL": return 7;
            default: return 3;
        }
    }

    // ========== GETTERS ET SETTERS UNIQUEMENT ==========

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