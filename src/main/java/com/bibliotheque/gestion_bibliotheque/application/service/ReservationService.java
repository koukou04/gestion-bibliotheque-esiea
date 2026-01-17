package com.bibliotheque.gestion_bibliotheque.application.service;

import com.bibliotheque.gestion_bibliotheque.adapters.messaging.event.ReservationCreeEvent;
import com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer.ReservationEventProducer;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Livre;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Membre;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Reservation;
import com.bibliotheque.gestion_bibliotheque.domain.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LivreService livreService;
    private final MembreService membreService;
    private final ReservationEventProducer reservationEventProducer;

    public ReservationService(ReservationRepository reservationRepository,
                              LivreService livreService,
                              MembreService membreService,
                              ReservationEventProducer reservationEventProducer) {
        this.reservationRepository = reservationRepository;
        this.livreService = livreService;
        this.membreService = membreService;
        this.reservationEventProducer = reservationEventProducer;
    }

    // === USE CASE: Réserver un livre ===
    public Reservation reserverLivre(Long livreId, Long membreId) {
        Livre livre = livreService.trouverLivreParId(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        if (livreService.estDisponible(livreId)) {
            throw new IllegalStateException("Le livre est disponible, pas besoin de réserver");
        }

        int position = reservationRepository.countByLivreIdAndStatut(livreId, "EN_ATTENTE") + 1;
        LocalDate dateReservation = LocalDate.now();
        Reservation reservation = new Reservation(null, livreId, membreId, dateReservation, position);
        Reservation reservationSauvegardee = reservationRepository.save(reservation);

        if (reservationEventProducer != null) {
            try {
                Membre membre = membreService.trouverMembreParId(membreId)
                        .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé"));

                ReservationCreeEvent event = new ReservationCreeEvent(
                        reservationSauvegardee.getId(),
                        livre.getId(),
                        membre.getId(),
                        livre.getTitre(),
                        membre.getNom() + " " + membre.getPrenom(),
                        membre.getEmail(),
                        dateReservation,
                        position
                );
                reservationEventProducer.publierReservation(event);
            } catch (Exception e) {
                System.err.println("Erreur lors de la publication de l'événement Kafka: " + e.getMessage());
            }
        }

        return reservationSauvegardee;
    }

    // === USE CASE: Annuler une réservation ===
    public Reservation annulerReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        if (!"EN_ATTENTE".equals(reservation.getStatut()) &&
                !"DISPONIBLE".equals(reservation.getStatut())) {
            throw new IllegalStateException("Cette réservation ne peut pas être annulée");
        }

        // ✅ Logique métier déplacée ICI
        reservation.setStatut("ANNULEE");
        return reservationRepository.save(reservation);
    }

    // === USE CASE: Notifier qu'un livre est disponible ===
    public void notifierProchaineReservation(Long livreId) {
        List<Reservation> reservationsEnAttente =
                reservationRepository.findByLivreIdAndStatutOrderByPosition(livreId, "EN_ATTENTE");

        if (!reservationsEnAttente.isEmpty()) {
            Reservation premiereReservation = reservationsEnAttente.get(0);

            // ✅ Logique métier déplacée ICI
            premiereReservation.setStatut("DISPONIBLE");
            premiereReservation.setDateExpiration(LocalDate.now().plusDays(3));

            reservationRepository.save(premiereReservation);
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
            // ✅ Logique métier déplacée ICI
            if (reservation.getDateExpiration() != null &&
                    LocalDate.now().isAfter(reservation.getDateExpiration())) {

                reservation.setStatut("EXPIREE");
                reservationRepository.save(reservation);
                notifierProchaineReservation(reservation.getLivreId());
            }
        }
    }
}