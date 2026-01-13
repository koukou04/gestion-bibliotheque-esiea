package com.bibliotheque.gestion_bibliotheque.application.service;


import com.bibliotheque.gestion_bibliotheque.domain.entities.Membre;
import com.bibliotheque.gestion_bibliotheque.domain.repository.MembreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembreService {

    private final MembreRepository membreRepository;

    // Injection de dépendances via constructeur
    public MembreService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    // === USE CASE: Inscrire un nouveau membre ===
    public Membre inscrireMembre(Membre membre) {
        // Vérifier que l'email n'existe pas déjà
        if (membreRepository.existsByEmail(membre.getEmail())) {
            throw new IllegalArgumentException("Un membre avec cet email existe déjà: " + membre.getEmail());
        }

        // Calculer le quota selon le type
        membre.setQuotaEmprunt(calculerQuota(membre.getTypeMembre()));

        // Sauvegarder le membre
        return membreRepository.save(membre);
    }

    // === MÉTHODE MÉTIER: Calculer le quota selon le type de membre ===
    private Integer calculerQuota(String type) {
        if (type == null) return 3;
        switch (type.toUpperCase()) {
            case "ETUDIANT": return 5;
            case "ENSEIGNANT": return 10;
            case "PERSONNEL": return 7;
            default: return 3;
        }
    }

    // === USE CASE: Trouver un membre par ID ===
    public Optional<Membre> trouverMembreParId(Long id) {
        return membreRepository.findById(id);
    }

    // === USE CASE: Trouver un membre par email ===
    public Optional<Membre> trouverMembreParEmail(String email) {
        return membreRepository.findByEmail(email);
    }

    // === USE CASE: Obtenir tous les membres ===
    public List<Membre> obtenirTousLesMembres() {
        return membreRepository.findAll();
    }

    // === USE CASE: Rechercher des membres par nom ===
    public List<Membre> rechercherParNom(String nom) {
        return membreRepository.findByNomContaining(nom);
    }

    // === USE CASE: Obtenir les membres par type ===
    public List<Membre> obtenirMembresParType(String typeMembre) {
        return membreRepository.findByTypeMembre(typeMembre);
    }

    // === USE CASE: Modifier un membre ===
    public Membre modifierMembre(Long id, Membre membreModifie) {
        Membre membre = membreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé avec l'ID: " + id));

        // Mettre à jour les champs
        membre.setNom(membreModifie.getNom());
        membre.setPrenom(membreModifie.getPrenom());
        membre.setEmail(membreModifie.getEmail());
        membre.setTypeMembre(membreModifie.getTypeMembre());
        
        // Recalculer le quota si le type a changé
        membre.setQuotaEmprunt(calculerQuota(membreModifie.getTypeMembre()));

        return membreRepository.save(membre);
    }

    // === USE CASE: Supprimer un membre ===
    public void supprimerMembre(Long id) {
        if (!membreRepository.existsById(id)) {
            throw new IllegalArgumentException("Membre non trouvé avec l'ID: " + id);
        }
        membreRepository.deleteById(id);
    }

    // === USE CASE: Calculer le score de fiabilité ===
    public void ajusterScore(Long membreId, int points) {
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé"));

        // Logique métier déplacée depuis l'entité
        int nouveauScore = membre.getScoreFiabilite() + points;
        if (nouveauScore < 0) {
            nouveauScore = 0;
        }
        if (nouveauScore > 100) {
            nouveauScore = 100;
        }
        membre.setScoreFiabilite(nouveauScore);
        
        membreRepository.save(membre);
    }

    // === MÉTHODE MÉTIER: Vérifier si un membre peut emprunter ===
    public boolean peutEmprunter(Long membreId, int nombreEmpruntsEnCours) {
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé"));

        // Logique métier déplacée depuis l'entité
        return nombreEmpruntsEnCours < membre.getQuotaEmprunt();
    }

    // === MÉTHODE MÉTIER: Obtenir le quota d'un membre ===
    public Integer obtenirQuota(Long membreId) {
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé"));

        return membre.getQuotaEmprunt();
    }

    // === MÉTHODE MÉTIER: Obtenir le score d'un membre ===
    public Integer obtenirScore(Long membreId) {
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé"));

        return membre.getScoreFiabilite();
    }
}
