# Système de Gestion de Bibliothèque

Projet d'Architecture d'Application - ESIEA Master 1

## 📚 Description

Application de gestion de bibliothèque développée avec Spring Boot et Clean Architecture. Permet de gérer un catalogue de livres, les membres, les emprunts, les réservations et fournit des statistiques sur l'utilisation de la bibliothèque.

## 👥 Équipe

- Messai
- Ramanadane  
- Ouallii

## 🏗️ Architecture

Le projet suit les principes de la **Clean Architecture** avec 4 couches principales :

### 1. Domain (Cœur métier)
- **Entities** : Livre, Membre, Emprunt, Reservation
- **Repositories** : Interfaces des repositories

### 2. Application (Use Cases)
- **Services** : Logique métier
- **DTOs** : Objets de transfert de données
- **Mappers** : Conversion entre entités et DTOs

### 3. Adapters (Interface)
- **Controllers REST** : Exposition des APIs
- **Repository Adapters** : Implémentation JPA
- **Infrastructure Entities** : Entités JPA

### 4. Frameworks
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database (développement)
- Swagger/OpenAPI

## 🚀 Fonctionnalités

### ✅ Gestion des Livres
- Ajouter, modifier, supprimer des livres
- Recherche par titre, auteur, ISBN, catégorie
- Gestion des stocks et disponibilité
- Suivi de l'état physique (neuf, bon état, abîmé, perdu)

### ✅ Gestion des Membres
- Inscription et gestion des profils
- Types de membres : ETUDIANT, ENSEIGNANT, PERSONNEL
- Quotas d'emprunt différenciés (5, 10, 7 livres)
- Système de score de fiabilité (0-100)

### ✅ Gestion des Emprunts
- Création d'emprunts avec date de retour automatique (14 jours)
- Suivi des emprunts en cours et en retard
- Calcul automatique des pénalités (1€/jour)
- Mise à jour du score selon les retours

### ✅ Gestion des Réservations
- Réserver un livre non disponible
- File d'attente automatique
- Notification automatique lors de la disponibilité
- Délai de retrait de 3 jours

### ✅ Statistiques et Rapports
- Tableau de bord général
- Top 5 des livres les plus empruntés
- Statistiques par catégorie
- Taux de retard global

## 📋 Prérequis

- Java 17
- Maven 3.8+
- Git

## 🔧 Installation

1. Cloner le projet
```bash
git clone https://gitlab.esiea.fr/ramanadane/gestion_bibliotheque_messai_ramanadane_ouallii.git
cd gestion_bibliotheque_messai_ramanadane_ouallii
```

2. Compiler le projet
```bash
./mvnw clean install
```

3. Lancer l'application
```bash
./mvnw spring-boot:run
```

L'application sera accessible sur : `http://localhost:8080`

## 📖 Documentation API

Une fois l'application lancée, la documentation Swagger est disponible sur :

**Swagger UI** : http://localhost:8080/swagger-ui.html

## 🗄️ Base de Données

### Mode Développement (H2)
- URL Console H2 : http://localhost:8080/h2-console
- JDBC URL : `jdbc:h2:mem:bibliotheque`
- Username : `sa`
- Password : _(vide)_

### Tables créées automatiquement
- `livres` : Catalogue des livres
- `membres` : Liste des membres
- `emprunts` : Historique des emprunts
- `reservations` : File d'attente des réservations

## 📡 Endpoints principaux

### Livres
- `GET /api/livres` - Liste tous les livres
- `POST /api/livres` - Ajouter un livre
- `GET /api/livres/{id}` - Détails d'un livre
- `PUT /api/livres/{id}` - Modifier un livre
- `DELETE /api/livres/{id}` - Supprimer un livre
- `GET /api/livres/disponibles` - Livres disponibles
- `GET /api/livres/recherche/titre?titre=xxx` - Recherche par titre

### Membres
- `GET /api/membres` - Liste tous les membres
- `POST /api/membres` - Inscrire un membre
- `GET /api/membres/{id}` - Détails d'un membre
- `PUT /api/membres/{id}` - Modifier un membre
- `GET /api/membres/{id}/score` - Score de fiabilité

### Emprunts
- `POST /api/emprunts?livreId=X&membreId=Y` - Emprunter un livre
- `PUT /api/emprunts/{id}/retour` - Retourner un livre
- `GET /api/emprunts/en-cours` - Emprunts en cours
- `GET /api/emprunts/en-retard` - Emprunts en retard
- `GET /api/emprunts/membre/{id}` - Emprunts d'un membre

### Réservations
- `POST /api/reservations?livreId=X&membreId=Y` - Réserver un livre
- `DELETE /api/reservations/{id}/annuler` - Annuler une réservation
- `GET /api/reservations/membre/{id}` - Réservations d'un membre
- `GET /api/reservations/livre/{id}` - File d'attente d'un livre

### Statistiques
- `GET /api/statistiques/dashboard` - Tableau de bord
- `GET /api/statistiques/livres-populaires` - Top 5 livres
- `GET /api/statistiques/par-categorie` - Stats par catégorie
- `GET /api/statistiques/taux-retard` - Taux de retard

## 💡 Règles Métier

### Quotas d'emprunt
- **Étudiant** : 5 livres maximum
- **Enseignant** : 10 livres maximum
- **Personnel** : 7 livres maximum

### Score de fiabilité
- Score initial : 50 points
- Retour à temps : +5 points
- Retour en retard : -10 points
- Livre abîmé : -20 points
- Score min/max : 0-100

### Emprunts
- Durée standard : 14 jours
- Pénalité de retard : 1€ par jour
- Calcul automatique à la date de retour

### Réservations
- Possibles uniquement si le livre n'est pas disponible
- Position automatique dans la file d'attente
- Délai de retrait : 3 jours après notification
- Expiration automatique si non retiré

## 🧪 Tests

Pour exécuter les tests :
```bash
./mvnw test
```

## 📦 Structure du Projet

```
src/
├── main/
│   ├── java/com/bibliotheque/gestion_bibliotheque/
│   │   ├── domain/                    # Couche Domain
│   │   │   ├── entities/              # Entités métier
│   │   │   └── repository/            # Interfaces repositories
│   │   ├── application/               # Couche Application
│   │   │   ├── service/               # Use cases
│   │   │   ├── dto/                   # DTOs
│   │   │   └── mapper/                # Mappers
│   │   └── adapters/                  # Couche Adapters
│   │       ├── controller/            # REST Controllers
│   │       ├── repository/            # Implémentation repositories
│   │       └── infrastructure/        # JPA entities & repositories
│   └── resources/
│       └── application.properties     # Configuration
└── test/                              # Tests unitaires
```

## 🔍 Technologies utilisées

- **Backend** : Spring Boot 2.7.18
- **Langage** : Java 17
- **Build Tool** : Maven
- **Base de données** : H2 (dev) / PostgreSQL (prod)
- **ORM** : Spring Data JPA + Hibernate
- **Documentation API** : Springdoc OpenAPI (Swagger)
- **Architecture** : Clean Architecture

## 📝 Notes de développement

### Points d'amélioration possibles
- [ ] Authentification JWT
- [ ] Système de notifications par email
- [ ] Tests unitaires et d'intégration
- [ ] Pagination des résultats
- [ ] Gestion des utilisateurs (bibliothécaires)
- [ ] Export des statistiques en PDF/Excel
- [ ] Interface frontend React/Vue.js

## 📄 Licence

Projet académique - ESIEA 2026

## 📞 Contact

Pour toute question sur le projet, contacter l'équipe via GitLab.
