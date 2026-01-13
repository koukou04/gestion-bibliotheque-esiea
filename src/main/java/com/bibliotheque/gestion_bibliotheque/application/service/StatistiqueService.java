package com.bibliotheque.gestion_bibliotheque.application.service;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Emprunt;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Livre;
import com.bibliotheque.gestion_bibliotheque.domain.repository.EmpruntRepository;
import com.bibliotheque.gestion_bibliotheque.domain.repository.LivreRepository;
import com.bibliotheque.gestion_bibliotheque.domain.repository.MembreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatistiqueService {

    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;
    private final MembreRepository membreRepository;

    public StatistiqueService(EmpruntRepository empruntRepository,
                             LivreRepository livreRepository,
                             MembreRepository membreRepository) {
        this.empruntRepository = empruntRepository;
        this.livreRepository = livreRepository;
        this.membreRepository = membreRepository;
    }

    // === STATISTIQUE: Nombre total d'emprunts ===
    public long getNombreTotalEmprunts() {
        return empruntRepository.findAll().size();
    }

    // === STATISTIQUE: Nombre d'emprunts en cours ===
    public long getNombreEmpruntsEnCours() {
        return empruntRepository.findByStatut("EN_COURS").size();
    }

    // === STATISTIQUE: Nombre d'emprunts en retard ===
    public long getNombreEmpruntsEnRetard() {
        return empruntRepository.findByStatutAndDateRetourPrevueBefore("EN_COURS", LocalDate.now()).size();
    }

    // === STATISTIQUE: Les 5 livres les plus empruntés ===
    public List<Map<String, Object>> getLivresLesPlusEmpruntes() {
        List<Emprunt> emprunts = empruntRepository.findAll();
        
        // Compter les emprunts par livre
        Map<Long, Long> compteurParLivre = emprunts.stream()
                .collect(Collectors.groupingBy(Emprunt::getLivreId, Collectors.counting()));

        // Trier et prendre les 5 premiers
        return compteurParLivre.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("livreId", entry.getKey());
                    
                    // Récupérer les infos du livre
                    livreRepository.findById(entry.getKey()).ifPresent(livre -> {
                        stat.put("titre", livre.getTitre());
                        stat.put("auteur", livre.getAuteur());
                    });
                    
                    stat.put("nombreEmprunts", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
    }

    // === STATISTIQUE: Taux de retard ===
    public double getTauxRetard() {
        long totalEmprunts = empruntRepository.findAll().size();
        if (totalEmprunts == 0) return 0.0;

        long empruntsEnRetard = empruntRepository.findAll().stream()
                .filter(emprunt -> estEnRetard(emprunt))
                .count();

        return (empruntsEnRetard * 100.0) / totalEmprunts;
    }

    // === MÉTHODE MÉTIER: Vérifier si un emprunt est en retard ===
    private boolean estEnRetard(Emprunt emprunt) {
        if (emprunt.getDateRetourEffective() != null) {
            return emprunt.getDateRetourEffective().isAfter(emprunt.getDateRetourPrevue());
        }
        return LocalDate.now().isAfter(emprunt.getDateRetourPrevue());
    }

    // === STATISTIQUE: Statistiques par catégorie ===
    public List<Map<String, Object>> getStatistiquesParCategorie() {
        List<Livre> livres = livreRepository.findAll();
        List<Emprunt> emprunts = empruntRepository.findAll();

        // Compter les emprunts par livre
        Map<Long, Long> empruntsParLivre = emprunts.stream()
                .collect(Collectors.groupingBy(Emprunt::getLivreId, Collectors.counting()));

        // Grouper par catégorie
        Map<String, Long> empruntsParCategorie = livres.stream()
                .collect(Collectors.groupingBy(
                        Livre::getCategorie,
                        Collectors.summingLong(livre -> 
                            empruntsParLivre.getOrDefault(livre.getId(), 0L))
                ));

        return empruntsParCategorie.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("categorie", entry.getKey());
                    stat.put("nombreEmprunts", entry.getValue());
                    return stat;
                })
                .collect(Collectors.toList());
    }

    // === STATISTIQUE: Tableau de bord général ===
    public Map<String, Object> getTableauDeBord() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("nombreTotalLivres", livreRepository.findAll().size());
        dashboard.put("nombreLivresDisponibles", livreRepository.findByNombreDisponiblesGreaterThan(0).size());
        dashboard.put("nombreTotalMembres", membreRepository.findAll().size());
        dashboard.put("nombreEmpruntsEnCours", getNombreEmpruntsEnCours());
        dashboard.put("nombreEmpruntsEnRetard", getNombreEmpruntsEnRetard());
        dashboard.put("tauxRetard", String.format("%.2f%%", getTauxRetard()));
        
        return dashboard;
    }
}
