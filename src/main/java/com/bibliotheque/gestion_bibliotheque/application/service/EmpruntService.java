package com.bibliotheque.gestion_bibliotheque.application.service;

import com.bibliotheque.gestion_bibliotheque.adapters.messaging.event.EmpruntCreeEvent;
import com.bibliotheque.gestion_bibliotheque.adapters.messaging.event.RetourLivreEvent;
import com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer.EmpruntEventProducer;
import com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer.RetourLivreEventProducer;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Emprunt;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Livre;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Membre;
import com.bibliotheque.gestion_bibliotheque.domain.repository.EmpruntRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final LivreService livreService;
    private final MembreService membreService;
    private final ReservationService reservationService;
    private final EmpruntEventProducer empruntEventProducer;
    private final RetourLivreEventProducer retourLivreEventProducer;

    public EmpruntService(EmpruntRepository empruntRepository,
                          LivreService livreService,
                          MembreService membreService,
                          ReservationService reservationService,
                          EmpruntEventProducer empruntEventProducer,
                          RetourLivreEventProducer retourLivreEventProducer) {
        this.empruntRepository = empruntRepository;
        this.livreService = livreService;
        this.membreService = membreService;
        this.reservationService = reservationService;
        this.empruntEventProducer = empruntEventProducer;
        this.retourLivreEventProducer = retourLivreEventProducer;
    }

    // === USE CASE: Emprunter un livre ===
    public Emprunt emprunterLivre(Long livreId, Long membreId) {
        // 1. Vérifier que le livre existe et est disponible
        if (!livreService.estDisponible(livreId)) {
            throw new IllegalStateException("Le livre n'est pas disponible");
        }

        // 2. Vérifier que le membre peut emprunter
        int empruntsEnCours = empruntRepository.countByMembreIdAndStatut(membreId, "EN_COURS");
        if (!membreService.peutEmprunter(membreId, empruntsEnCours)) {
            throw new IllegalStateException("Le membre a atteint son quota d'emprunts");
        }

        // 3. Créer l'emprunt (durée: 14 jours)
        LocalDate dateEmprunt = LocalDate.now();
        LocalDate dateRetourPrevue = dateEmprunt.plusDays(14);

        Emprunt emprunt = new Emprunt(null, livreId, membreId, dateEmprunt, dateRetourPrevue);

        // 4. Décrémenter le nombre d'exemplaires disponibles
        livreService.emprunterExemplaire(livreId);

        // 5. Sauvegarder l'emprunt
        Emprunt empruntSauvegarde = empruntRepository.save(emprunt);

        // 6. PUBLIER UN ÉVÉNEMENT KAFKA (Architecture Événementielle - EDA)
        if (empruntEventProducer != null) {
            try {
                Livre livre = livreService.trouverLivreParId(livreId).orElse(null);
                Membre membre = membreService.trouverMembreParId(membreId).orElse(null);

                if (livre != null && membre != null) {
                    EmpruntCreeEvent event = new EmpruntCreeEvent(
                            empruntSauvegarde.getId(),
                            livreId,
                            livre.getTitre(),
                            membreId,
                            membre.getNom() + " " + membre.getPrenom(),
                            LocalDateTime.now(),
                            dateRetourPrevue.atStartOfDay()
                    );
                    empruntEventProducer.publierEmpruntCree(event);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la publication de l'événement Kafka: " + e.getMessage());
            }
        }

        return empruntSauvegarde;
    }

    // === USE CASE: Retourner un livre ===
    public Emprunt retournerLivre(Long empruntId) {
        // 1. Récupérer l'emprunt
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new IllegalArgumentException("Emprunt non trouvé"));

        if (!"EN_COURS".equals(emprunt.getStatut())) {
            throw new IllegalStateException("Cet emprunt n'est pas en cours");
        }

        // 2. ✅ LOGIQUE MÉTIER: Calculer le retour
        LocalDate dateRetourEffective = LocalDate.now();
        emprunt.setDateRetourEffective(dateRetourEffective);

        // Vérifier si en retard
        boolean enRetard = dateRetourEffective.isAfter(emprunt.getDateRetourPrevue());

        // Calculer pénalité
        Double penalite = 0.0;
        if (enRetard) {
            long joursDeRetard = java.time.temporal.ChronoUnit.DAYS.between(
                    emprunt.getDateRetourPrevue(), dateRetourEffective
            );
            penalite = joursDeRetard * 1.0;
        }

        emprunt.setPenalite(penalite);

        // Mettre à jour le statut
        if (enRetard) {
            emprunt.setStatut("RETOURNE_EN_RETARD");
        } else {
            emprunt.setStatut("RETOURNE");
        }

        // 3. Incrémenter le nombre d'exemplaires disponibles
        livreService.retournerExemplaire(emprunt.getLivreId());

        // 4. Ajuster le score du membre
        if (enRetard) {
            membreService.ajusterScore(emprunt.getMembreId(), -10);
        } else {
            membreService.ajusterScore(emprunt.getMembreId(), 5);
        }

        // 5. Notifier la prochaine réservation s'il y en a une
        reservationService.notifierProchaineReservation(emprunt.getLivreId());

        // 6. Sauvegarder l'emprunt
        Emprunt empruntRetourne = empruntRepository.save(emprunt);

        // 7. PUBLIER UN ÉVÉNEMENT KAFKA (RetourLivre)
        if (retourLivreEventProducer != null) {
            try {
                Livre livre = livreService.trouverLivreParId(emprunt.getLivreId()).orElse(null);
                Membre membre = membreService.trouverMembreParId(emprunt.getMembreId()).orElse(null);

                if (livre != null && membre != null) {
                    RetourLivreEvent event = new RetourLivreEvent(
                            empruntRetourne.getId(),
                            livre.getId(),
                            membre.getId(),
                            livre.getTitre(),
                            membre.getNom() + " " + membre.getPrenom(),
                            emprunt.getDateRetourPrevue(),
                            dateRetourEffective,
                            enRetard,
                            penalite
                    );
                    retourLivreEventProducer.publierRetourLivre(event);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la publication de l'événement Kafka: " + e.getMessage());
            }
        }

        return empruntRetourne;
    }

    // === USE CASE: Obtenir les emprunts d'un membre ===
    public List<Emprunt> obtenirEmpruntsParMembre(Long membreId) {
        return empruntRepository.findByMembreId(membreId);
    }

    // === USE CASE: Obtenir les emprunts en cours ===
    public List<Emprunt> obtenirEmpruntsEnCours() {
        return empruntRepository.findByStatut("EN_COURS");
    }

    // === USE CASE: Obtenir les emprunts en retard ===
    public List<Emprunt> obtenirEmpruntsEnRetard() {
        return empruntRepository.findByStatutAndDateRetourPrevueBefore(
                "EN_COURS", LocalDate.now()
        );
    }

    // === USE CASE: Obtenir un emprunt par ID ===
    public Optional<Emprunt> trouverEmpruntParId(Long id) {
        return empruntRepository.findById(id);
    }

    // === MÉTHODE MÉTIER: Vérifier si un emprunt est en retard ===
    public boolean estEnRetard(Emprunt emprunt) {
        if (emprunt.getDateRetourEffective() != null) {
            return emprunt.getDateRetourEffective().isAfter(emprunt.getDateRetourPrevue());
        }
        return LocalDate.now().isAfter(emprunt.getDateRetourPrevue());
    }

    // === MÉTHODE MÉTIER: Calculer les jours de retard ===
    public int calculerJoursDeRetard(Emprunt emprunt) {
        LocalDate dateReference = emprunt.getDateRetourEffective() != null
                ? emprunt.getDateRetourEffective()
                : LocalDate.now();

        if (dateReference.isAfter(emprunt.getDateRetourPrevue())) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(
                    emprunt.getDateRetourPrevue(), dateReference
            );
        }
        return 0;
    }
}