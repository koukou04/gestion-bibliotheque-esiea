# 📸 GUIDE DES CAPTURES D'ÉCRAN KAFKA

## Contexte
La professeure demande dans l'Exercice 6 et dans les livrables :
- **"Capture Kafka (production + consommation)"**

Voici exactement comment obtenir ces captures pour prouver que Kafka fonctionne.

---

## 🎬 SCÉNARIO COMPLET À CAPTURER

### Étape 1 : Démarrer Docker Compose (si pas déjà fait)
```powershell
docker-compose up -d
docker-compose ps
```

**📸 CAPTURE 1 : État des containers**
- Capturez la sortie de `docker-compose ps` montrant les 3 services running (zookeeper, kafka, backend)

---

### Étape 2 : Créer un emprunt pour déclencher Kafka

```powershell
# Créer un emprunt via l'API REST
Invoke-WebRequest -Uri "http://localhost:8080/api/emprunts?livreId=1&membreId=1" -Method POST -ContentType "application/json"
```

**Résultat attendu :** 
```json
{
  "id": 1,
  "membreId": 1,
  "livreId": 1,
  "dateEmprunt": "2026-01-09",
  "dateRetourPrevue": "2026-01-23",
  "dateRetourEffective": null,
  "statut": "EN_COURS"
}
```

---

### Étape 3 : Capturer les logs du PRODUCTEUR Kafka

```powershell
# Afficher les logs du backend filtrant les événements publiés
docker-compose logs backend | Select-String "publié"
```

**📸 CAPTURE 2 : Production Kafka (Exercice 3)**

**Ce que vous devez voir dans les logs :**
```
bibliotheque-backend  | Événement EmpruntCree publié dans Kafka: EmpruntCreeEvent{
bibliotheque-backend  |   empruntId=1, 
bibliotheque-backend  |   livreId=1, 
bibliotheque-backend  |   titreLivre='Clean Architecture', 
bibliotheque-backend  |   membreId=1, 
bibliotheque-backend  |   nomMembre='Dupont Jean', 
bibliotheque-backend  |   dateEmprunt=2026-01-09, 
bibliotheque-backend  |   dateRetourPrevue=2026-01-23
bibliotheque-backend  | }
```

**📌 Capture requise :** Terminal PowerShell montrant cette ligne de log avec :
- ✅ Nom du topic : `emprunts-topic`
- ✅ Détails de l'événement publié
- ✅ Message "publié dans Kafka"

---

### Étape 4 : Capturer les logs du CONSOMMATEUR Kafka

```powershell
# Afficher les logs du backend filtrant les événements reçus
docker-compose logs backend | Select-String "Événement Kafka"
```

**📸 CAPTURE 3 : Consommation Kafka (Exercice 4)**

**Ce que vous devez voir dans les logs :**
```
bibliotheque-backend  | === Événement Kafka reçu ===
bibliotheque-backend  | Traitement de l'événement EmpruntCree:
bibliotheque-backend  | - ID Emprunt: 1
bibliotheque-backend  | - Livre: Clean Architecture (ID: 1)
bibliotheque-backend  | - Membre: Dupont Jean (ID: 1)
bibliotheque-backend  | - Date emprunt: 2026-01-09
bibliotheque-backend  | - Date retour prévue: 2026-01-23
bibliotheque-backend  | ✓ Événement traité avec succès
```

**📌 Capture requise :** Terminal PowerShell montrant :
- ✅ "=== Événement Kafka reçu ===" (preuve que le consumer écoute)
- ✅ Détails de l'événement reçu (ID emprunt, livre, membre)
- ✅ "✓ Événement traité avec succès" (preuve du traitement)

---

## 🎯 ALTERNATIVE : Capture complète des logs

Si vous préférez une seule capture montrant PRODUCTION + CONSOMMATION :

```powershell
# Récupérer les 50 dernières lignes des logs
docker-compose logs backend --tail=50
```

Puis faites défiler jusqu'à voir :
1. **La publication** : `"Événement EmpruntCree publié dans Kafka"`
2. **La réception** : `"=== Événement Kafka reçu ==="`
3. **Le traitement** : `"✓ Événement traité avec succès"`

**📸 CAPTURE UNIQUE** : Une seule capture d'écran du terminal montrant les 3 étapes dans l'ordre.

---

## 📋 CHECKLIST FINALE

Vos captures doivent prouver :

✅ **Production Kafka (Exercice 3)** :
- [ ] Événement publié dans le topic `emprunts-topic`
- [ ] Log contenant `EmpruntCreeEvent{...}`
- [ ] Message "publié dans Kafka"

✅ **Consommation Kafka (Exercice 4)** :
- [ ] Événement reçu par le `@KafkaListener`
- [ ] Log contenant "Événement Kafka reçu"
- [ ] Détails de l'événement traité
- [ ] Message "Événement traité avec succès"

---

## 💡 CONSEILS

1. **Utilisez PowerShell ISE** pour un meilleur rendu visuel des captures
2. **Zoomez** sur les lignes importantes dans vos captures
3. **Annotez** vos captures avec des flèches pointant vers :
   - Le message "publié dans Kafka" (Production)
   - Le message "Événement Kafka reçu" (Consommation)
4. **Format recommandé** : PNG ou JPG en haute résolution

---

## 📄 FICHIERS À INCLURE DANS LE LIVRABLE

```
📁 Livrables_Kafka/
  ├── capture_1_containers_running.png
  ├── capture_2_production_kafka.png
  ├── capture_3_consommation_kafka.png
  └── (optionnel) capture_complete_producteur_consommateur.png
```

---

## 🔗 PREUVES ALTERNATIVES

Si vous avez des soucis avec les captures d'écran, vous pouvez aussi fournir :
- Le fichier `VALIDATION_DOCKER_COMPLETE.md` qui contient tous les logs
- Une vidéo screencast montrant la séquence complète
- Les logs bruts exportés : `docker-compose logs backend > logs_kafka.txt`

---

**✅ Votre projet est conforme aux exigences de l'Exercice 3, 4 et 6 !**

Date de validation : 2026-01-09
Binôme : Messai, Ramanadane, Ouallii
Projet : Gestion Bibliothèque - Clean Architecture + Kafka + Docker
