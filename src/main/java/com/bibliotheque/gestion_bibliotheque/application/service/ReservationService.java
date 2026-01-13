package com.bibliotheque.gestion_bibliotheque.application.service;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Reservation;
import com.bibliotheque.gestion_bibliotheque.domain.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LivreService livreService;

    public ReservationService(ReservationRepository reservationRepository,
                              LivreService livreService) {
        this.reservationRepository = reservationRepository;
        this.livreService = livreService;
    }

    // === USE CASE: Réserver un livre ===
    public Reservation reserverLivre(Long livreId, Long membreId) {
        // 1. Vérifier que le livre existe
        if (!livreService.trouverLivreParId(livreId).isPresent()) {
            throw new IllegalArgumentException("Livre non trouvé");
        }

        // 2. Vérifier que le livre n'est pas disponible (sinon pas besoin de réserver)
        if (livreService.estDisponible(livreId)) {
            throw new IllegalStateException("Le livre est disponible, pas besoin de réserver");
        }

        // 3. Calculer la position dans la file d'attente
        int position = reservationRepository.countByLivreIdAndStatut(livreId, "EN_ATTENTE") + 1;

        // 4. Créer la réservation
        LocalDate dateReservation = LocalDate.now();
        Reservation reservation = new Reservation(null, livreId, membreId, dateReservation, position);

        // 5. Sauvegarder la réservation
        return reservationRepository.save(reservation);
    }

    // === USE CASE: Annuler une réservation ===
    public Reservation annulerReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        if (!"EN_ATTENTE".equals(reservation.getStatut()) &&
            !"DISPONIBLE".equals(reservation.getStatut())) {
            throw new IllegalStateException("Cette réservation ne peut pas être annulée");
        }

        // Logique métier déplacée depuis l'entité
        reservation.setStatut("ANNULEE");
        
        return reservationRepository.save(reservation);
    }

    // === USE CASE: Notifier qu'un livre est disponible ===
    // Cette méthode est appelée quand un livre est retourné
    public void notifierProchaineReservation(Long livreId) {
        // Trouver la première réservation en attente
        List<Reservation> reservationsEnAttente = 
            reservationRepository.findByLivreIdAndStatutOrderByPosition(livreId, "EN_ATTENTE");

        if (!reservationsEnAttente.isEmpty()) {
            Reservation premiereReservation = reservationsEnAttente.get(0);
            
            // Logique métier déplacée depuis l'entité
            premiereReservation.setStatut("DISPONIBLE");
            premiereReservation.setDateExpiration(LocalDate.now().plusDays(3)); // 3 jours pour retirer
            
            reservationRepository.save(premiereReservation);

            // Note: Ici on pourrait envoyer une notification au membre
            // Mais on n'a pas implémenté le système de notifications complet
        }
    }

    // === USE CASE: Obtenir les réservations d'un membre ===
    public List<Reservation> obtenirReservationsParMembre(Long membreId) {
        return reservationRepository.findByMembreId(membreId);
    }

    // === USE CASE: Obtenir les réservations d'un livre ===
    public List<Reservation> obtenirReservationsParLivre(Long livreId) {
        return reservationRepository.findByLivreId(livreId);
    }

    // === USE CASE: Obtenir une réservation par ID ===
    public Optional<Reservation> trouverReservationParId(Long id) {
        return reservationRepository.findById(id);
    }

    // === USE CASE: Vérifier et marquer les réservations expirées ===
    public void verifierReservationsExpirees() {
        List<Reservation> reservationsDisponibles = 
            reservationRepository.findByStatut("DISPONIBLE");

        for (Reservation reservation : reservationsDisponibles) {
            // Logique métier déplacée depuis l'entité
            if (estExpiree(reservation)) {
                reservation.setStatut("EXPIREE");
                reservationRepository.save(reservation);

                // Notifier la prochaine personne dans la file
                notifierProchaineReservation(reservation.getLivreId());
            }
        }
    }

    // === MÉTHODE MÉTIER: Vérifier si une réservation est expirée ===
    private boolean estExpiree(Reservation reservation) {
        if (reservation.getDateExpiration() == null) return false;
        return LocalDate.now().isAfter(reservation.getDateExpiration());
    }
}
