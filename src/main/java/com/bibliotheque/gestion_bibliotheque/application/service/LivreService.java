package com.bibliotheque.gestion_bibliotheque.application.service;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Livre;
import com.bibliotheque.gestion_bibliotheque.domain.repository.LivreRepository;

import java.util.List;
import java.util.Optional;

public class LivreService {

    private final LivreRepository livreRepository;

    public LivreService(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    // === USE CASE: Ajouter un livre au catalogue ===
    public Livre ajouterLivre(Livre livre) {
        if (livreRepository.findByIsbn(livre.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Un livre avec cet ISBN existe déjà: " + livre.getIsbn());
        }
        return livreRepository.save(livre);
    }

    // === USE CASE: Rechercher un livre par ID ===
    public Optional<Livre> trouverLivreParId(Long id) {
        return livreRepository.findById(id);
    }

    // === USE CASE: Rechercher un livre par ISBN ===
    public Optional<Livre> trouverLivreParIsbn(String isbn) {
        return livreRepository.findByIsbn(isbn);
    }

    // === USE CASE: Rechercher des livres par titre ===
    public List<Livre> rechercherParTitre(String titre) {
        return livreRepository.findByTitreContaining(titre);
    }

    // === USE CASE: Rechercher des livres par auteur ===
    public List<Livre> rechercherParAuteur(String auteur) {
        return livreRepository.findByAuteurContaining(auteur);
    }

    // === USE CASE: Rechercher des livres par catégorie ===
    public List<Livre> rechercherParCategorie(String categorie) {
        return livreRepository.findByCategorie(categorie);
    }

    // === USE CASE: Obtenir tous les livres ===
    public List<Livre> obtenirTousLesLivres() {
        return livreRepository.findAll();
    }

    // === USE CASE: Obtenir les livres disponibles ===
    public List<Livre> obtenirLivresDisponibles() {
        return livreRepository.findByNombreDisponiblesGreaterThan(0);
    }

    // === USE CASE: Modifier un livre ===
    public Livre modifierLivre(Long id, Livre livreModifie) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé avec l'ID: " + id));

        livre.setTitre(livreModifie.getTitre());
        livre.setAuteur(livreModifie.getAuteur());
        livre.setEditeur(livreModifie.getEditeur());
        livre.setAnneePublication(livreModifie.getAnneePublication());
        livre.setCategorie(livreModifie.getCategorie());
        livre.setNombreExemplaires(livreModifie.getNombreExemplaires());
        livre.setEtatPhysique(livreModifie.getEtatPhysique());

        return livreRepository.save(livre);
    }

    // === USE CASE: Supprimer un livre ===
    public void supprimerLivre(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new IllegalArgumentException("Livre non trouvé avec l'ID: " + id);
        }
        livreRepository.deleteById(id);
    }

    // === MÉTHODE MÉTIER: Vérifier si un livre est disponible ===
    public boolean estDisponible(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));
        // ✅ Logique métier déplacée ICI
        return livre.getNombreDisponibles() != null && livre.getNombreDisponibles() > 0;
    }

    // === MÉTHODE MÉTIER: Emprunter un exemplaire ===
    public void emprunterExemplaire(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        // ✅ Logique métier déplacée ICI
        if (livre.getNombreDisponibles() == null || livre.getNombreDisponibles() <= 0) {
            throw new IllegalStateException("Aucun exemplaire disponible pour le livre: " + livre.getTitre());
        }

        livre.setNombreDisponibles(livre.getNombreDisponibles() - 1);
        livreRepository.save(livre);
    }

    // === MÉTHODE MÉTIER: Retourner un exemplaire ===
    public void retournerExemplaire(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        // ✅ Logique métier déplacée ICI
        if (livre.getNombreDisponibles() >= livre.getNombreExemplaires()) {
            throw new IllegalStateException("Erreur: tous les exemplaires sont déjà en stock");
        }

        livre.setNombreDisponibles(livre.getNombreDisponibles() + 1);
        livreRepository.save(livre);
    }
}