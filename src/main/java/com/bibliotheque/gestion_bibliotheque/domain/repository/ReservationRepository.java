package com.bibliotheque.gestion_bibliotheque.domain.repository;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    // Créer ou mettre à jour une réservation
    Reservation save(Reservation reservation);

    // Trouver une réservation par ID
    Optional<Reservation> findById(Long id);

    // Trouver toutes les réservations
    List<Reservation> findAll();

    // Trouver les réservations d'un membre
    List<Reservation> findByMembreId(Long membreId);

    // Trouver les réservations d'un livre
    List<Reservation> findByLivreId(Long livreId);

    // Trouver les réservations en attente pour un livre (ordonnées par position)
    List<Reservation> findByLivreIdAndStatutOrderByPosition(Long livreId, String statut);

    // Trouver les réservations par statut
    List<Reservation> findByStatut(String statut);

    // Compter les réservations en attente pour un livre
    int countByLivreIdAndStatut(Long livreId, String statut);

    // Supprimer une réservation
    void deleteById(Long id);

    // Vérifier si une réservation existe
    boolean existsById(Long id);
}
