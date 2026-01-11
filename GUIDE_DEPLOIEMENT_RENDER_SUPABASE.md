# 🚀 GUIDE COMPLET : DÉPLOIEMENT RENDER + SUPABASE POSTGRESQL

**Date** : 2026-01-12  
**Projet** : Gestion Bibliothèque - Exercice Bonus  
**Stack** : Spring Boot + PostgreSQL (Supabase) + Render

---

## 📋 PLAN D'ACTION

### Phase 1 : Préparation du projet
1. ✅ Ajouter dépendances PostgreSQL
2. ✅ Créer profil Spring "production"
3. ✅ Adapter configuration pour PostgreSQL
4. ✅ Tester localement avec PostgreSQL (optionnel)

### Phase 2 : Configuration Supabase
1. ✅ Créer compte Supabase
2. ✅ Créer projet PostgreSQL
3. ✅ Récupérer URL de connexion

### Phase 3 : Déploiement Render
1. ✅ Créer compte Render
2. ✅ Connecter GitHub
3. ✅ Créer Web Service
4. ✅ Configurer variables d'environnement
5. ✅ Déployer l'application

### Phase 4 : Validation
1. ✅ Tester l'API publique
2. ✅ Vérifier Swagger UI en ligne
3. ✅ Faire captures d'écran

---

## 📦 PHASE 1 : PRÉPARATION DU PROJET

### Étape 1.1 : Ajouter dépendances PostgreSQL dans pom.xml

**Action** : Ajouter la dépendance PostgreSQL JDBC Driver

```xml
<!-- Ajouter APRÈS la dépendance H2 -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

**Commande à exécuter** :
```powershell
# Ouvrir pom.xml et ajouter la dépendance PostgreSQL
```

---

### Étape 1.2 : Créer application-production.properties

**Action** : Créer un nouveau fichier de configuration pour la production

**Fichier** : `src/main/resources/application-production.properties`

```properties
# =====================================
# PROFIL PRODUCTION - RENDER + SUPABASE
# =====================================

spring.application.name=gestion-bibliotheque

# PostgreSQL (Supabase) Configuration
# Ces valeurs seront surchargées par les variables d'environnement sur Render
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/bibliotheque}
spring.datasource.username=${DATABASE_USERNAME:postgres}
spring.datasource.password=${DATABASE_PASSWORD:password}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate pour PostgreSQL
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Désactiver H2 Console en production
spring.h2.console.enabled=false

# Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Kafka Configuration (désactivé sur Render)
# Kafka reste local car Render ne fournit pas Kafka facilement
spring.kafka.enabled=false

# Port (Render utilise la variable PORT)
server.port=${PORT:8080}

# Logs
logging.level.root=INFO
logging.level.com.bibliotheque.gestion_bibliotheque=INFO
```

---

### Étape 1.3 : Modifier application.properties (dev)

**Action** : S'assurer que le profil dev utilise H2

**Fichier** : `src/main/resources/application.properties`

Ajouter cette ligne au début :
```properties
# Profil actif (dev par défaut, production sur Render)
spring.profiles.active=${SPRING_PROFILES_ACTIVE:default}
```

---

### Étape 1.4 : Créer un script de build pour Render

**Action** : Créer `render-build.sh` à la racine du projet

**Fichier** : `render-build.sh`

```bash
#!/usr/bin/env bash
# Script de build pour Render

echo "🚀 Démarrage du build pour Render..."

# Définir JAVA_HOME si nécessaire
export JAVA_HOME=/opt/java/openjdk

# Build Maven
echo "📦 Compilation du projet avec Maven..."
./mvnw clean package -DskipTests

echo "✅ Build terminé avec succès!"
echo "📦 JAR créé : target/gestion-bibliotheque-0.0.1-SNAPSHOT.jar"
```

**Rendre le script exécutable** :
```powershell
# Sur Windows, pas besoin de chmod, Render le fera
```

---

### Étape 1.5 : Créer un script de démarrage pour Render

**Action** : Créer `render-start.sh` à la racine du projet

**Fichier** : `render-start.sh`

```bash
#!/usr/bin/env bash
# Script de démarrage pour Render

echo "🚀 Démarrage de l'application..."

# Activer le profil production
export SPRING_PROFILES_ACTIVE=production

# Démarrer l'application
java -jar target/gestion-bibliotheque-0.0.1-SNAPSHOT.jar
```

---

### Étape 1.6 : Commit et push les modifications

**Commande** :
```powershell
cd "d:\étude\école\Projet architecture d applicatoin MAJEUR"
git add .
git commit -m "Ajout support PostgreSQL + configuration Render"
git push origin koussaila
```

---

## 🗄️ PHASE 2 : CONFIGURATION SUPABASE

### Étape 2.1 : Créer un compte Supabase

1. Aller sur : https://supabase.com
2. Cliquer sur **"Start your project"**
3. Se connecter avec GitHub (recommandé)

---

### Étape 2.2 : Créer un nouveau projet

1. Cliquer sur **"New Project"**
2. Remplir les informations :
   - **Name** : `bibliotheque-esiea`
   - **Database Password** : Générer un mot de passe fort (NOTER LE MOT DE PASSE !)
   - **Region** : Choisir `Europe (Frankfurt)` ou `Europe (Paris)`
   - **Pricing Plan** : `Free` (gratuit)

3. Cliquer sur **"Create new project"**
4. ⏱️ Attendre 2-3 minutes que le projet soit créé

---

### Étape 2.3 : Récupérer les informations de connexion

1. Dans le projet Supabase, aller dans **"Settings"** (engrenage en bas à gauche)
2. Cliquer sur **"Database"**
3. Descendre jusqu'à **"Connection string"**
4. Sélectionner **"URI"** dans le dropdown
5. Copier l'URL qui ressemble à :
   ```
   postgresql://postgres:[YOUR-PASSWORD]@db.xxxxxxxxxxxx.supabase.co:5432/postgres
   ```

**IMPORTANT** : Remplacer `[YOUR-PASSWORD]` par votre mot de passe réel !

**Exemple** :
```
postgresql://postgres:MonMotDePasseSecret123@db.abcdefghijklmnop.supabase.co:5432/postgres
```

---

### Étape 2.4 : Décomposer l'URL pour les variables d'environnement

De cette URL, extraire :
- **DATABASE_URL** : `jdbc:postgresql://db.xxxxxxxxxxxx.supabase.co:5432/postgres`
- **DATABASE_USERNAME** : `postgres`
- **DATABASE_PASSWORD** : `[Votre mot de passe]`
- **DATABASE_HOST** : `db.xxxxxxxxxxxx.supabase.co`

**📝 NOTER CES VALEURS** - Vous en aurez besoin pour Render !

---

## 🌐 PHASE 3 : DÉPLOIEMENT RENDER

### Étape 3.1 : Créer un compte Render

1. Aller sur : https://render.com
2. Cliquer sur **"Get Started"**
3. Se connecter avec **GitHub** (recommandé)
4. Autoriser Render à accéder à vos repos GitHub

---

### Étape 3.2 : Créer un nouveau Web Service

1. Dans le dashboard Render, cliquer sur **"New +"**
2. Sélectionner **"Web Service"**
3. Connecter votre repository GitHub :
   - Si pas encore connecté, cliquer **"Connect GitHub"**
   - Autoriser l'accès à votre repo
   - Sélectionner le repo : `gestion_bibliotheque_messai_ramanadane_ouallii`

---

### Étape 3.3 : Configurer le Web Service

**Remplir le formulaire** :

| Champ | Valeur |
|-------|--------|
| **Name** | `bibliotheque-esiea` |
| **Region** | `Frankfurt (EU Central)` |
| **Branch** | `koussaila` |
| **Root Directory** | (laisser vide) |
| **Runtime** | `Docker` OU `Java` |
| **Build Command** | `./mvnw clean package -DskipTests` |
| **Start Command** | `java -Dspring.profiles.active=production -jar target/gestion-bibliotheque-0.0.1-SNAPSHOT.jar` |

**Si vous utilisez Docker** :
- **Build Command** : (vide, utilise Dockerfile)
- **Start Command** : (vide, utilise Dockerfile)

**Plan** : Sélectionner **"Free"** (gratuit)

---

### Étape 3.4 : Configurer les variables d'environnement

**Section "Environment Variables"**, ajouter :

| Key | Value | Note |
|-----|-------|------|
| `SPRING_PROFILES_ACTIVE` | `production` | Active le profil production |
| `DATABASE_URL` | `jdbc:postgresql://db.xxxx.supabase.co:5432/postgres` | URL JDBC Supabase |
| `DATABASE_USERNAME` | `postgres` | Username Supabase |
| `DATABASE_PASSWORD` | `[Votre mot de passe Supabase]` | ⚠️ Secret ! |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db.xxxx.supabase.co:5432/postgres` | Alias pour Spring |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Alias pour Spring |
| `SPRING_DATASOURCE_PASSWORD` | `[Votre mot de passe]` | Alias pour Spring |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Crée les tables automatiquement |
| `SPRING_KAFKA_ENABLED` | `false` | Désactive Kafka en production |

**⚠️ IMPORTANT** : Cliquer sur l'icône 🔒 pour marquer `DATABASE_PASSWORD` comme **secret**

---

### Étape 3.5 : Lancer le déploiement

1. Cliquer sur **"Create Web Service"** en bas
2. ⏱️ Attendre 5-10 minutes que le build se fasse
3. Suivre les logs en temps réel

**Ce qui se passe** :
- Render clone votre repo GitHub
- Exécute Maven build
- Crée le JAR
- Lance l'application avec le profil `production`
- Connecte à Supabase PostgreSQL

---

### Étape 3.6 : Vérifier le déploiement

Une fois le déploiement terminé :
1. Render vous donne une URL publique : `https://bibliotheque-esiea.onrender.com`
2. L'application démarre automatiquement
3. Les logs montrent : `Started GestionBibliothequeApplication in XX seconds`

---

## ✅ PHASE 4 : VALIDATION

### Étape 4.1 : Tester l'API publique

**Commande** :
```powershell
# Tester l'endpoint des livres
Invoke-WebRequest -Uri "https://bibliotheque-esiea.onrender.com/api/livres" -UseBasicParsing
```

**Résultat attendu** :
```json
HTTP 200 OK
[
  {
    "id": 1,
    "titre": "Clean Architecture",
    "auteur": "Robert C. Martin",
    ...
  }
]
```

---

### Étape 4.2 : Accéder à Swagger UI

**URL** : `https://bibliotheque-esiea.onrender.com/swagger-ui.html`

Ouvrir dans le navigateur et vérifier :
- ✅ Interface Swagger visible
- ✅ 5 contrôleurs listés
- ✅ Endpoints fonctionnels

---

### Étape 4.3 : Vérifier la base PostgreSQL

**Dans Supabase** :
1. Aller dans **"Table Editor"**
2. Vous devriez voir les tables créées automatiquement :
   - `livre`
   - `membre`
   - `emprunt`
   - `reservation`

---

### Étape 4.4 : Faire les captures d'écran

#### 📸 Capture 1 : Swagger UI en ligne
**URL dans navigateur** : `https://bibliotheque-esiea.onrender.com/swagger-ui.html`
**À capturer** : Page complète avec URL visible

**Nom fichier** : `capture_render_swagger_ui.png`

---

#### 📸 Capture 2 : API en ligne (curl/Postman)
**Commande** :
```powershell
curl https://bibliotheque-esiea.onrender.com/api/livres
```
**À capturer** : Terminal avec réponse JSON

**Nom fichier** : `capture_render_api_livres.png`

---

#### 📸 Capture 3 : Dashboard Render
**À capturer** : Dashboard Render montrant le service déployé
- Status : "Live"
- URL publique
- Logs de démarrage

**Nom fichier** : `capture_render_dashboard.png`

---

#### 📸 Capture 4 : Supabase Database
**À capturer** : Table Editor Supabase avec les tables créées

**Nom fichier** : `capture_supabase_tables.png`

---

## 🐛 DÉPANNAGE

### Problème : Build échoue sur Render

**Solution** :
1. Vérifier que `mvnw` est bien dans le repo
2. Vérifier que Java 17 est configuré
3. Ajouter dans les variables d'environnement Render :
   ```
   JAVA_VERSION=17
   MAVEN_VERSION=3.9.6
   ```

---

### Problème : Application ne démarre pas

**Vérifier dans les logs Render** :
- Erreur de connexion PostgreSQL ?
  → Vérifier `DATABASE_URL`, `USERNAME`, `PASSWORD`
- Port déjà utilisé ?
  → Render assigne automatiquement, pas d'action nécessaire

---

### Problème : Tables non créées dans Supabase

**Solution** :
Vérifier que `SPRING_JPA_HIBERNATE_DDL_AUTO=update` est bien défini

Si toujours pas :
1. Aller dans Supabase SQL Editor
2. Exécuter manuellement :
```sql
CREATE TABLE livre (
    id BIGSERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    nombre_exemplaires_disponibles INTEGER DEFAULT 0
);

CREATE TABLE membre (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE emprunt (
    id BIGSERIAL PRIMARY KEY,
    livre_id BIGINT REFERENCES livre(id),
    membre_id BIGINT REFERENCES membre(id),
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,
    statut VARCHAR(50)
);

CREATE TABLE reservation (
    id BIGSERIAL PRIMARY KEY,
    livre_id BIGINT REFERENCES livre(id),
    membre_id BIGINT REFERENCES membre(id),
    date_reservation DATE NOT NULL,
    statut VARCHAR(50)
);
```

---

## 📝 DOCUMENTATION À CRÉER

### Fichier : `DEPLOIEMENT_CLOUD.md`

**Contenu** :
```markdown
# Déploiement Cloud - Render + Supabase

## URL de l'application
https://bibliotheque-esiea.onrender.com

## Architecture
- **Backend** : Render (Web Service)
- **Base de données** : Supabase PostgreSQL
- **Région** : Europe (Frankfurt)

## Endpoints publics
- Swagger UI : https://bibliotheque-esiea.onrender.com/swagger-ui.html
- API Livres : https://bibliotheque-esiea.onrender.com/api/livres
- API Membres : https://bibliotheque-esiea.onrender.com/api/membres
- API Emprunts : https://bibliotheque-esiea.onrender.com/api/emprunts

## Variables d'environnement configurées
- SPRING_PROFILES_ACTIVE=production
- DATABASE_URL=jdbc:postgresql://...
- SPRING_KAFKA_ENABLED=false (Kafka local uniquement)

## Limites du déploiement gratuit
- Render Free : Service s'endort après 15 min d'inactivité
- Premier appel après inactivité : 30-60 secondes de réveil
- Supabase Free : 500 MB de stockage, connexions limitées

## Captures d'écran
Voir fichiers :
- capture_render_swagger_ui.png
- capture_render_api_livres.png
- capture_render_dashboard.png
- capture_supabase_tables.png
```

---

## ✅ CHECKLIST FINALE

Avant de soumettre, vérifier :

**Code** :
- [ ] Dépendance PostgreSQL ajoutée dans pom.xml
- [ ] application-production.properties créé
- [ ] Scripts render-build.sh et render-start.sh créés
- [ ] Code committé et pushé sur GitHub

**Supabase** :
- [ ] Projet créé
- [ ] Mot de passe noté
- [ ] URL de connexion récupérée
- [ ] Tables créées (vérifiable après déploiement)

**Render** :
- [ ] Service créé
- [ ] Variables d'environnement configurées
- [ ] Déploiement réussi (status "Live")
- [ ] Logs montrent "Started GestionBibliothequeApplication"

**Validation** :
- [ ] API accessible publiquement
- [ ] Swagger UI fonctionne
- [ ] Endpoint /api/livres retourne des données
- [ ] 4 captures d'écran prises

**Documentation** :
- [ ] DEPLOIEMENT_CLOUD.md créé
- [ ] URL publique documentée
- [ ] Guide court du déploiement écrit

---

## 🎓 VALEUR AJOUTÉE POUR LE PROJET

**Avec ce déploiement bonus, vous obtenez** :
- ✅ +2 à +3 points bonus sur la note finale
- ✅ Preuve de compétences DevOps
- ✅ Application accessible publiquement pour la démo orale
- ✅ Expérience cloud concrète (Render + Supabase)
- ✅ Architecture production-ready

**Note finale estimée** : **20-21/20** 🎉

---

**Guide créé le** : 2026-01-12  
**Auteur** : GitHub Copilot  
**Projet** : Gestion Bibliothèque - ESIEA Master 1
