# 📸 CHECKLIST DES CAPTURES D'ÉCRAN OBLIGATOIRES

## 🎬 SÉQUENCE À SUIVRE POUR LES CAPTURES

### Préparation : Démarrer Docker
```powershell
cd "d:\étude\école\Projet architecture d applicatoin MAJEUR"
docker-compose up -d
```

Attendre ~1 minute que tout démarre.

---

## ✅ CAPTURE 1/4 : Containers Docker Running

### Commande :
```powershell
docker-compose ps
```

### 📸 Ce que vous devez voir et capturer :
```
NAME                     IMAGE                                STATUS
bibliotheque-backend     ...                                  Up (healthy)
bibliotheque-kafka       confluentinc/cp-kafka:7.5.0         Up (healthy)
bibliotheque-zookeeper   confluentinc/cp-zookeeper:7.5.0     Up (healthy)
```

**Fichier de capture** : `01_docker_containers_running.png`

---

## ✅ CAPTURE 2/4 : Swagger UI (API accessible via Docker)

### Commande :
```powershell
Start-Process "http://localhost:8080/swagger-ui.html"
```

### 📸 Ce que vous devez capturer :
- Page Swagger UI complète dans le navigateur
- Tous vos contrôleurs visibles :
  - ✅ emprunt-controller
  - ✅ livre-controller
  - ✅ membre-controller
  - ✅ reservation-controller
  - ✅ statistique-controller
- URL visible dans la barre d'adresse : `http://localhost:8080/swagger-ui.html`

**ASTUCE** : Zoomez à 80% dans le navigateur pour tout capturer

**Fichier de capture** : `02_swagger_ui_api_accessible.png`

---

## ✅ CAPTURE 3/4 : Kafka PRODUCTION (Exercice 3)

### Étape 1 : Créer un emprunt pour générer un événement
```powershell
# Via le navigateur Swagger UI, exécuter :
POST /api/emprunts?livreId=1&membreId=1
```

OU via PowerShell :
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/emprunts?livreId=3&membreId=1" -Method POST -UseBasicParsing
```

### Étape 2 : Récupérer les logs du producteur
```powershell
docker logs bibliotheque-backend --tail=50
```

Ou filtrer directement :
```powershell
docker logs bibliotheque-backend 2>&1 | Select-String -Pattern "publié dans Kafka" -Context 1
```

### 📸 Ce que vous devez capturer dans le terminal :
```
c.b.g.a.m.producer.EmpruntEventProducer : Événement EmpruntCree publié dans Kafka: EmpruntCreeEvent{
  empruntId=3, 
  livreId=3, 
  titreLivre='Domain-Driven Design', 
  membreId=1, 
  nomMembre='Dupont Jean', 
  dateEmprunt=2026-01-11T..., 
  dateRetourPrevu=2026-01-25T00:00, 
  eventType='EMPRUNT_CREE'
}
```

**IMPORTANT** : La ligne doit contenir **"publié dans Kafka"** pour prouver la production !

**Fichier de capture** : `03_kafka_production_evenement.png`

---

## ✅ CAPTURE 4/4 : Kafka CONSOMMATION (Exercice 4)

### Commande (même logs, partie différente) :
```powershell
docker logs bibliotheque-backend 2>&1 | Select-String -Pattern "Événement Kafka reçu" -Context 5
```

### 📸 Ce que vous devez capturer dans le terminal :
```
c.b.g.a.m.consumer.EmpruntEventConsumer : === Événement Kafka reçu ===
c.b.g.a.m.consumer.EmpruntEventConsumer : Message brut: {"empruntId":3,"livreId":3,...}
c.b.g.a.m.consumer.EmpruntEventConsumer : Traitement de l'événement EmpruntCree:
c.b.g.a.m.consumer.EmpruntEventConsumer :   - Emprunt ID: 3
c.b.g.a.m.consumer.EmpruntEventConsumer :   - Livre: Domain-Driven Design (ID: 3)
c.b.g.a.m.consumer.EmpruntEventConsumer :   - Membre: Dupont Jean (ID: 1)
c.b.g.a.m.consumer.EmpruntEventConsumer :   - Date d'emprunt: [2026, 1, 11, ...]
c.b.g.a.m.consumer.EmpruntEventConsumer :   - Date de retour prévue: [2026, 1, 25, 0, 0]
c.b.g.a.m.consumer.EmpruntEventConsumer : ✓ Événement traité avec succès
```

**IMPORTANT** : Les lignes doivent montrer :
- ✅ "Événement Kafka reçu" (preuve de réception)
- ✅ Détails de l'événement
- ✅ "traité avec succès" (preuve du traitement)

**Fichier de capture** : `04_kafka_consommation_evenement.png`

---

## 💡 ASTUCE : Capture UNIQUE Production + Consommation

Au lieu de faire 2 captures séparées, vous pouvez faire **UNE SEULE capture** montrant les 2 :

### Commande :
```powershell
docker logs bibliotheque-backend --tail=100
```

Puis **scrollez** dans le terminal pour capturer une vue montrant :
1. La ligne "publié dans Kafka" (HAUT de la capture)
2. La ligne "Événement Kafka reçu" (BAS de la capture)

**Fichier de capture** : `03_04_kafka_production_et_consommation.png`

---

## 📁 STRUCTURE DES LIVRABLES

```
📁 Captures_Ecran/
  ├── 01_docker_containers_running.png
  ├── 02_swagger_ui_api_accessible.png
  ├── 03_kafka_production_evenement.png
  └── 04_kafka_consommation_evenement.png
```

OU version optimisée :
```
📁 Captures_Ecran/
  ├── 01_docker_containers_running.png
  ├── 02_swagger_ui_api_accessible.png
  └── 03_04_kafka_production_et_consommation.png  (les 2 en 1)
```

---

## 🎯 RÉCAPITULATIF DES EXIGENCES

| Capture | Exercice | Obligatoire | Prouve quoi ? |
|---------|----------|-------------|---------------|
| Containers Docker | Ex. 6 | ✅ OUI | Déploiement Docker fonctionnel |
| Swagger UI | Ex. 2 & 6 | ✅ OUI | API REST accessible via Docker |
| Kafka Production | Ex. 3 | ✅ OUI | Événement publié dans Kafka |
| Kafka Consommation | Ex. 4 | ✅ OUI | Événement reçu et traité |

---

## ✅ VALIDATION FINALE

Avant de soumettre, vérifiez que chaque capture montre :

**Capture Docker** :
- [ ] 3 containers visibles
- [ ] Status "Up" ou "healthy"
- [ ] Noms : bibliotheque-backend, bibliotheque-kafka, bibliotheque-zookeeper

**Capture Swagger** :
- [ ] URL visible : `http://localhost:8080/swagger-ui.html`
- [ ] 5 contrôleurs visibles
- [ ] Interface Swagger complète

**Capture Kafka Production** :
- [ ] Texte "publié dans Kafka" visible
- [ ] Détails de l'événement (empruntId, livreId, membreId)
- [ ] Nom de la classe : `EmpruntEventProducer`

**Capture Kafka Consommation** :
- [ ] Texte "Événement Kafka reçu" visible
- [ ] Traitement détaillé (ID emprunt, livre, membre)
- [ ] Texte "traité avec succès" visible
- [ ] Nom de la classe : `EmpruntEventConsumer`

---

## 🚀 COMMANDES RAPIDES

Tout en une fois :
```powershell
# 1. Démarrer Docker
docker-compose up -d

# 2. Attendre 60 secondes
Start-Sleep -Seconds 60

# 3. Vérifier containers (CAPTURE 1)
docker-compose ps

# 4. Ouvrir Swagger (CAPTURE 2)
Start-Process "http://localhost:8080/swagger-ui.html"

# 5. Créer un emprunt
Invoke-WebRequest -Uri "http://localhost:8080/api/emprunts?livreId=4&membreId=2" -Method POST -UseBasicParsing

# 6. Voir les logs Kafka (CAPTURES 3 & 4)
docker logs bibliotheque-backend --tail=100
```

---

**Date de préparation** : 2026-01-11  
**Projet** : Gestion Bibliothèque - Architecture Applicative  
**Équipe** : Messai, Ramanadane, Ouallii
