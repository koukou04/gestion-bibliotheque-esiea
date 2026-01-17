package com.bibliotheque.gestion_bibliotheque.adapters.controller;

import com.bibliotheque.gestion_bibliotheque.application.dto.EmpruntDto;
import com.bibliotheque.gestion_bibliotheque.application.mapper.EmpruntMapper;
import com.bibliotheque.gestion_bibliotheque.application.service.EmpruntService;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Emprunt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprunts")
@Tag(name = "Gestion des Emprunts", description = "APIs pour gérer les emprunts de livres")
public class EmpruntController {

    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    // === EMPRUNTER UN LIVRE ===
    @PostMapping
    @Operation(summary = "Emprunter un livre", description = "Crée un nouvel emprunt pour un membre et un livre")
    public ResponseEntity<EmpruntDto> emprunterLivre(
            @RequestParam Long livreId,
            @RequestParam Long membreId) {
        try {
            Emprunt emprunt = empruntService.emprunterLivre(livreId, membreId);
            return ResponseEntity.status(HttpStatus.CREATED).body(EmpruntMapper.toDto(emprunt));
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Retourne 400 Bad Request si le livre n'est pas disponible ou le membre ne peut pas emprunter
            return ResponseEntity.badRequest().build();
        }
    }

    // === RETOURNER UN LIVRE ===
    @PutMapping("/{empruntId}/retour")
    @Operation(summary = "Retourner un livre", description = "Enregistre le retour d'un livre emprunté")
    public ResponseEntity<EmpruntDto> retournerLivre(@PathVariable Long empruntId) {
        try {
            Emprunt emprunt = empruntService.retournerLivre(empruntId);
            return ResponseEntity.ok(EmpruntMapper.toDto(emprunt));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // === OBTENIR LES EMPRUNTS (avec filtres optionnels) ===
    @GetMapping
    @Operation(summary = "Obtenir les emprunts", description = "Récupère les emprunts avec filtres optionnels (membreId, statut: en-cours, en-retard)")
    public ResponseEntity<List<EmpruntDto>> obtenirEmprunts(
            @RequestParam(required = false) Long membreId,
            @RequestParam(required = false) String statut) {

        List<Emprunt> emprunts;

        if (membreId != null) {
            emprunts = empruntService.obtenirEmpruntsParMembre(membreId);
        } else if ("en-cours".equals(statut)) {
            emprunts = empruntService.obtenirEmpruntsEnCours();
        } else if ("en-retard".equals(statut)) {
            emprunts = empruntService.obtenirEmpruntsEnRetard();
        } else {
            emprunts = empruntService.obtenirEmpruntsEnCours(); // Par défaut, retourne les emprunts en cours
        }

        return ResponseEntity.ok(EmpruntMapper.toDtoList(emprunts));
    }

    // === OBTENIR UN EMPRUNT PAR ID ===
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un emprunt par ID", description = "Récupère les détails d'un emprunt spécifique")
    public ResponseEntity<EmpruntDto> obtenirEmpruntParId(@PathVariable Long id) {
        return empruntService.trouverEmpruntParId(id)
                .map(emprunt -> ResponseEntity.ok(EmpruntMapper.toDto(emprunt)))
                .orElse(ResponseEntity.notFound().build());
    }
}
