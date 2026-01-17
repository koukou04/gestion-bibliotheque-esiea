package com.bibliotheque.gestion_bibliotheque.adapters.controller;

import com.bibliotheque.gestion_bibliotheque.application.dto.MembreDto;
import com.bibliotheque.gestion_bibliotheque.application.mapper.MembreMapper;
import com.bibliotheque.gestion_bibliotheque.application.service.MembreService;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Membre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membres")
@Tag(name = "Gestion des Membres", description = "APIs pour gérer les membres de la bibliothèque")
public class MembreController {

    private final MembreService membreService;

    public MembreController(MembreService membreService) {
        this.membreService = membreService;
    }

    // === INSCRIRE UN NOUVEAU MEMBRE ===
    @PostMapping
    @Operation(summary = "Inscrire un nouveau membre", description = "Crée un nouveau compte membre dans le système")
    public ResponseEntity<MembreDto> inscrireMembre(@RequestBody MembreDto membreDto) {
        try {
            Membre membre = MembreMapper.toEntity(membreDto);
            Membre membreInscrit = membreService.inscrireMembre(membre);
            return ResponseEntity.status(HttpStatus.CREATED).body(MembreMapper.toDto(membreInscrit));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // === OBTENIR LES MEMBRES (avec filtres optionnels) ===
    @GetMapping
    @Operation(summary = "Obtenir les membres", description = "Récupère les membres avec filtres optionnels (email, typeMembre)")
    public ResponseEntity<List<MembreDto>> obtenirMembres(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String typeMembre) {

        // Si email est fourni, retourner le membre correspondant (ou liste vide)
        if (email != null) {
            return membreService.trouverMembreParEmail(email)
                    .map(membre -> ResponseEntity.ok(List.of(MembreMapper.toDto(membre))))
                    .orElse(ResponseEntity.ok(List.of()));
        }

        List<Membre> membres;
        if (typeMembre != null) {
            membres = membreService.obtenirMembresParType(typeMembre);
        } else {
            membres = membreService.obtenirTousLesMembres();
        }

        return ResponseEntity.ok(MembreMapper.toDtoList(membres));
    }

    // === OBTENIR UN MEMBRE PAR ID ===
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un membre par ID", description = "Récupère les informations d'un membre spécifique")
    public ResponseEntity<MembreDto> obtenirMembreParId(@PathVariable Long id) {
        return membreService.trouverMembreParId(id)
                .map(membre -> ResponseEntity.ok(MembreMapper.toDto(membre)))
                .orElse(ResponseEntity.notFound().build());
    }

    // === MODIFIER UN MEMBRE ===
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un membre", description = "Met à jour les informations d'un membre existant")
    public ResponseEntity<MembreDto> modifierMembre(@PathVariable Long id, @RequestBody MembreDto membreDto) {
        try {
            Membre membre = MembreMapper.toEntity(membreDto);
            Membre membreModifie = membreService.modifierMembre(id, membre);
            return ResponseEntity.ok(MembreMapper.toDto(membreModifie));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === SUPPRIMER UN MEMBRE ===
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un membre", description = "Supprime un membre du système")
    public ResponseEntity<Void> supprimerMembre(@PathVariable Long id) {
        try {
            membreService.supprimerMembre(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === OBTENIR LE SCORE D'UN MEMBRE ===
    @GetMapping("/{id}/score")
    @Operation(summary = "Obtenir le score de fiabilité", description = "Récupère le score de fiabilité d'un membre")
    public ResponseEntity<Integer> obtenirScore(@PathVariable Long id) {
        try {
            int score = membreService.obtenirScore(id);
            return ResponseEntity.ok(score);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
