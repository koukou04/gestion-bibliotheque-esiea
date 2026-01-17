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

    // === USE CASE: Ajouter un livre ===
    public Livre ajouterLivre(Livre livre) {
        // Vérifier que l'ISBN n'existe pas déjà
        if (livre.getIsbn() != null && livreRepository.findByIsbn(livre.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Un livre avec cet ISBN existe déjà");
        }
        return livreRepository.save(livre);
    }

    // === USE CASE: Modifier un livre ===
    public Livre modifierLivre(Long id, Livre livreModifie) {
        Livre livreExistant = livreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        livreExistant.setTitre(livreModifie.getTitre());
        livreExistant.setAuteur(livreModifie.getAuteur());
        livreExistant.setIsbn(livreModifie.getIsbn());
        livreExistant.setEditeur(livreModifie.getEditeur());
        livreExistant.setAnneePublication(livreModifie.getAnneePublication());
        livreExistant.setCategorie(livreModifie.getCategorie());
        livreExistant.setNombreExemplaires(livreModifie.getNombreExemplaires());
        livreExistant.setNombreDisponibles(livreModifie.getNombreDisponibles());
        livreExistant.setEtatPhysique(livreModifie.getEtatPhysique());

        return livreRepository.save(livreExistant);
    }

    // === USE CASE: Supprimer un livre ===
    public void supprimerLivre(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new IllegalArgumentException("Livre non trouvé");
        }
        livreRepository.deleteById(id);
    }

    // === USE CASE: Obtenir tous les livres ===
    public List<Livre> obtenirTousLesLivres() {
        return livreRepository.findAll();
    }

    // === USE CASE: Trouver un livre par ID ===
    public Optional<Livre> trouverLivreParId(Long id) {
        return livreRepository.findById(id);
    }

    // === USE CASE: Rechercher par titre ===
    public List<Livre> rechercherParTitre(String titre) {
        return livreRepository.findByTitreContaining(titre);
    }

    // === USE CASE: Rechercher par auteur ===
    public List<Livre> rechercherParAuteur(String auteur) {
        return livreRepository.findByAuteurContaining(auteur);
    }

    // === USE CASE: Rechercher par catégorie ===
    public List<Livre> rechercherParCategorie(String categorie) {
        return livreRepository.findByCategorie(categorie);
    }

    // === USE CASE: Obtenir les livres disponibles ===
    public List<Livre> obtenirLivresDisponibles() {
        return livreRepository.findByNombreDisponiblesGreaterThan(0);
    }

    // === MÉTHODE MÉTIER: Vérifier si un livre est disponible ===
    public boolean estDisponible(Long livreId) {
        return livreRepository.findById(livreId)
                .map(livre -> livre.getNombreDisponibles() > 0)
                .orElse(false);
    }

    // === MÉTHODE MÉTIER: Emprunter un exemplaire ===
    public void emprunterExemplaire(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        if (livre.getNombreDisponibles() <= 0) {
            throw new IllegalStateException("Aucun exemplaire disponible");
        }

        livre.setNombreDisponibles(livre.getNombreDisponibles() - 1);
        livreRepository.save(livre);
    }

    // === MÉTHODE MÉTIER: Retourner un exemplaire ===
    public void retournerExemplaire(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        livre.setNombreDisponibles(livre.getNombreDisponibles() + 1);
        livreRepository.save(livre);
    }
}
