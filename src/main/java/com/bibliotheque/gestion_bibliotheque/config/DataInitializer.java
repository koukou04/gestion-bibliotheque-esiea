package com.bibliotheque.gestion_bibliotheque.config;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Livre;
import com.bibliotheque.gestion_bibliotheque.domain.entities.Membre;
import com.bibliotheque.gestion_bibliotheque.application.service.LivreService;
import com.bibliotheque.gestion_bibliotheque.application.service.MembreService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(LivreService livreService, MembreService membreService) {
        return args -> {
            // Vérifier si des livres existent déjà (pour éviter les doublons en production)
            if (livreService.obtenirTousLesLivres().isEmpty()) {
                // Ajouter quelques livres de test
                livreService.ajouterLivre(new Livre(
                    null, 
                    "Clean Architecture", 
                    "Robert C. Martin", 
                    "978-0134494166",
                    "Prentice Hall", 
                    2017, 
                    "Informatique",
                    3, 
                    "NEUF"
            ));

            livreService.ajouterLivre(new Livre(
                    null,
                    "Design Patterns",
                    "Gang of Four",
                    "978-0201633610",
                    "Addison-Wesley",
                    1994,
                    "Informatique",
                    2,
                    "BON_ETAT"
            ));

            livreService.ajouterLivre(new Livre(
                    null,
                    "Le Petit Prince",
                    "Antoine de Saint-Exupéry",
                    "978-2070612758",
                    "Gallimard",
                    1943,
                    "Fiction",
                    5,
                    "NEUF"
            ));

            livreService.ajouterLivre(new Livre(
                    null,
                    "1984",
                    "George Orwell",
                    "978-2070368228",
                    "Gallimard",
                    1949,
                    "Fiction",
                    4,
                    "BON_ETAT"
            ));

            livreService.ajouterLivre(new Livre(
                    null,
                    "Introduction aux Algorithmes",
                    "Thomas H. Cormen",
                    "978-2100545261",
                    "Dunod",
                    2010,
                    "Informatique",
                    2,
                    "NEUF"
            ));

            // Ajouter quelques membres de test
            membreService.inscrireMembre(new Membre(
                    null,
                    "Dupont",
                    "Jean",
                    "jean.dupont@esiea.fr",
                    "ETUDIANT"
            ));

            membreService.inscrireMembre(new Membre(
                    null,
                    "Martin",
                    "Sophie",
                    "sophie.martin@esiea.fr",
                    "ENSEIGNANT"
            ));

            membreService.inscrireMembre(new Membre(
                    null,
                    "Bernard",
                    "Luc",
                    "luc.bernard@esiea.fr",
                    "PERSONNEL"
            ));

                System.out.println("✅ Données de test chargées avec succès !");
            } else {
                System.out.println("ℹ️ Données déjà présentes en base, chargement ignoré.");
            }
        };
    }
}
