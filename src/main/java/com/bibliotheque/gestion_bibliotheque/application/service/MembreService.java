<<<<<<< HEAD:src/main/java/com/bibliotheque/gestion_bibliotheque/application/usecase/GestionMembreUseCase.java
package com.bibliotheque.gestion_bibliotheque.application.usecase;
=======
package com.bibliotheque.gestion_bibliotheque.application.service;
>>>>>>> koussaila:src/main/java/com/bibliotheque/gestion_bibliotheque/application/service/MembreService.java

import com.bibliotheque.gestion_bibliotheque.domain.entities.Membre;
import com.bibliotheque.gestion_bibliotheque.domain.repository.MembreRepository;

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD:src/main/java/com/bibliotheque/gestion_bibliotheque/application/usecase/GestionMembreUseCase.java
@Service
public class GestionMembreUseCase {

    private final MembreRepository membreRepository;

    public GestionMembreUseCase(MembreRepository membreRepository) {
=======
public class MembreService {

    private final MembreRepository membreRepository;

    public MembreService(MembreRepository membreRepository) {
>>>>>>> koussaila:src/main/java/com/bibliotheque/gestion_bibliotheque/application/service/MembreService.java
        this.membreRepository = membreRepository;
    }

    // === USE CASE: Inscrire un nouveau membre ===
    public Membre inscrireMembre(Membre membre) {
        if (membreRepository.existsByEmail(membre.getEmail())) {
            throw new IllegalArgumentException("Un membre avec cet email existe déjà: " + membre.getEmail());
        }
        return membreRepository.save(membre);
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

        membre.setNom(membreModifie.getNom());
        membre.setPrenom(membreModifie.getPrenom());
        membre.setEmail(membreModifie.getEmail());
        membre.setTypeMembre(membreModifie.getTypeMembre());

        return membreRepository.save(membre);
    }

    // === USE CASE: Supprimer un membre ===
    public void supprimerMembre(Long id) {
        if (!membreRepository.existsById(id)) {
            throw new IllegalArgumentException("Membre non trouvé avec l'ID: " + id);
        }
        membreRepository.deleteById(id);
    }

    // === MÉTHODE MÉTIER: Ajuster le score ===
    public void ajusterScore(Long membreId, int points) {
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé"));
<<<<<<< HEAD:src/main/java/com/bibliotheque/gestion_bibliotheque/application/usecase/GestionMembreUseCase.java
        membre.ajusterScore(points);
=======

        // ✅ Logique métier déplacée ICI
        int nouveauScore = membre.getScoreFiabilite() + points;

        if (nouveauScore < 0) {
            nouveauScore = 0;
        }
        if (nouveauScore > 100) {
            nouveauScore = 100;
        }

        membre.setScoreFiabilite(nouveauScore);
>>>>>>> koussaila:src/main/java/com/bibliotheque/gestion_bibliotheque/application/service/MembreService.java
        membreRepository.save(membre);
    }

    // === MÉTHODE MÉTIER: Vérifier si un membre peut emprunter ===
    public boolean peutEmprunter(Long membreId, int nombreEmpruntsEnCours) {
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé"));
<<<<<<< HEAD:src/main/java/com/bibliotheque/gestion_bibliotheque/application/usecase/GestionMembreUseCase.java
        return membre.peutEmprunter(nombreEmpruntsEnCours);
=======

        // ✅ Logique métier déplacée ICI
        return nombreEmpruntsEnCours < membre.getQuotaEmprunt();
>>>>>>> koussaila:src/main/java/com/bibliotheque/gestion_bibliotheque/application/service/MembreService.java
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