package com.bibliotheque.gestion_bibliotheque.adapters.controller;

import com.bibliotheque.gestion_bibliotheque.application.service.StatistiqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistiques")
@Tag(name = "Statistiques et Rapports", description = "APIs pour consulter les statistiques de la bibliothèque")
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    public StatistiqueController(StatistiqueService statistiqueService) {
        this.statistiqueService = statistiqueService;
    }

    // === TABLEAU DE BORD GÉNÉRAL ===
    @GetMapping("/dashboard")
    @Operation(summary = "Tableau de bord", description = "Récupère les statistiques générales de la bibliothèque")
    public ResponseEntity<Map<String, Object>> getTableauDeBord() {
        return ResponseEntity.ok(statistiqueService.getTableauDeBord());
    }

    // === LIVRES LES PLUS EMPRUNTÉS ===
    @GetMapping("/livres-populaires")
    @Operation(summary = "Livres les plus empruntés", description = "Récupère le top 5 des livres les plus empruntés")
    public ResponseEntity<List<Map<String, Object>>> getLivresLesPlusEmpruntes() {
        return ResponseEntity.ok(statistiqueService.getLivresLesPlusEmpruntes());
    }

    // === STATISTIQUES PAR CATÉGORIE ===
    @GetMapping("/par-categorie")
    @Operation(summary = "Statistiques par catégorie", description = "Récupère le nombre d'emprunts par catégorie de livres")
    public ResponseEntity<List<Map<String, Object>>> getStatistiquesParCategorie() {
        return ResponseEntity.ok(statistiqueService.getStatistiquesParCategorie());
    }

    // === TAUX DE RETARD ===
    @GetMapping("/taux-retard")
    @Operation(summary = "Taux de retard", description = "Calcule le pourcentage d'emprunts en retard")
    public ResponseEntity<Map<String, Double>> getTauxRetard() {
        double taux = statistiqueService.getTauxRetard();
        return ResponseEntity.ok(Map.of("tauxRetard", taux));
    }
}
