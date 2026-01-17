package com.bibliotheque.gestion_bibliotheque.adapters.controller;

import com.bibliotheque.gestion_bibliotheque.application.dto.ReservationDto;
import com.bibliotheque.gestion_bibliotheque.application.mapper.ReservationMapper;
import com.bibliotheque.gestion_bibliotheque.application.service.ReservationService;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Gestion des Réservations", description = "APIs pour gérer les réservations de livres")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // === RÉSERVER UN LIVRE ===
    @PostMapping
    @Operation(summary = "Réserver un livre", description = "Crée une nouvelle réservation pour un livre non disponible")
    public ResponseEntity<ReservationDto> reserverLivre(
            @RequestParam Long livreId,
            @RequestParam Long membreId) {
        try {
            Reservation reservation = reservationService.reserverLivre(livreId, membreId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ReservationMapper.toDto(reservation));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // === ANNULER UNE RÉSERVATION ===
    @DeleteMapping("/{reservationId}/annuler")
    @Operation(summary = "Annuler une réservation", description = "Annule une réservation existante")
    public ResponseEntity<ReservationDto> annulerReservation(@PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.annulerReservation(reservationId);
            return ResponseEntity.ok(ReservationMapper.toDto(reservation));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // === OBTENIR LES RÉSERVATIONS (avec filtres optionnels) ===
    @GetMapping
    @Operation(summary = "Obtenir les réservations", description = "Récupère les réservations avec filtres optionnels (membreId, livreId)")
    public ResponseEntity<List<ReservationDto>> obtenirReservations(
            @RequestParam(required = false) Long membreId,
            @RequestParam(required = false) Long livreId) {

        List<Reservation> reservations;

        if (membreId != null) {
            reservations = reservationService.obtenirReservationsParMembre(membreId);
        } else if (livreId != null) {
            reservations = reservationService.obtenirReservationsParLivre(livreId);
        } else {
            // Retourne une liste vide si aucun filtre n'est fourni (ou implémenter une méthode pour tout récupérer)
            reservations = List.of();
        }

        return ResponseEntity.ok(ReservationMapper.toDtoList(reservations));
    }

    // === OBTENIR UNE RÉSERVATION PAR ID ===
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une réservation par ID", description = "Récupère les détails d'une réservation spécifique")
    public ResponseEntity<ReservationDto> obtenirReservationParId(@PathVariable Long id) {
        return reservationService.trouverReservationParId(id)
                .map(reservation -> ResponseEntity.ok(ReservationMapper.toDto(reservation)))
                .orElse(ResponseEntity.notFound().build());
    }
}
