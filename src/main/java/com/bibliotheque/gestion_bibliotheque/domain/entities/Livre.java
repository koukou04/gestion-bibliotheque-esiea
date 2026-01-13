package com.bibliotheque.gestion_bibliotheque.domain.entities;


public class Livre {
    private Long id;
    private String titre;
    private String auteur;
    private String isbn;
    private String editeur;
    private Integer anneePublication;
    private String categorie;
    private Integer nombreExemplaires;
    private Integer nombreDisponibles;
    private String etatPhysique; // NEUF, BON_ETAT, ABIME, PERDU

    // Constructeur vide
    public Livre() {}

    // Constructeur complet
    public Livre(Long id, String titre, String auteur, String isbn,
                 String editeur, Integer anneePublication, String categorie,
                 Integer nombreExemplaires, String etatPhysique) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.editeur = editeur;
        this.anneePublication = anneePublication;
        this.categorie = categorie;
        this.nombreExemplaires = nombreExemplaires;
        this.nombreDisponibles = nombreExemplaires; // Au début tous dispo
        this.etatPhysique = etatPhysique;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }

    public Integer getAnneePublication() { return anneePublication; }
    public void setAnneePublication(Integer anneePublication) {
        this.anneePublication = anneePublication;
    }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public Integer getNombreExemplaires() { return nombreExemplaires; }
    public void setNombreExemplaires(Integer nombreExemplaires) {
        this.nombreExemplaires = nombreExemplaires;
    }

    public Integer getNombreDisponibles() { return nombreDisponibles; }
    public void setNombreDisponibles(Integer nombreDisponibles) {
        this.nombreDisponibles = nombreDisponibles;
    }

    public String getEtatPhysique() { return etatPhysique; }
    public void setEtatPhysique(String etatPhysique) {
        this.etatPhysique = etatPhysique;
    }
}