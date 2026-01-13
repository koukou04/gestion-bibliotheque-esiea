# 🚀 GUIDE POUR BHARGAVI - Tester le projet sans Kafka

## ✅ SOLUTION RAPIDE : Lancer SANS Kafka (H2 en mémoire)

### **Commandes à exécuter** :
```bash
# 1. Vérifier si Maven installé
./mvnw --version

# 2. Lancer l'application SANS Docker (SANS Kafka)
./mvnw spring-boot:run

# 3. Ouvrir dans navigateur
http://localhost:8080/swagger-ui.html
```

**L'application marchera SANS Kafka !**
- ✅ Base de données H2 en mémoire
- ✅ Tous les endpoints API fonctionnent
- ⚠️ Kafka désactivé (mais pas grave pour tester)

---

## 🐋 Si elle VEUT tester Kafka (optionnel)

### **Option A : Vérifier Docker**
```bash
# Taper dans PowerShell/CMD
docker --version

# Si ça répond → Docker installé ✅
# Si "command not found" → Docker pas installé ❌
```

### **Option B : Installer Docker Desktop**
1. Télécharger : https://www.docker.com/products/docker-desktop
2. Installer (redémarrage nécessaire)
3. Lancer Docker Desktop
4. Attendre 30 secondes

### **Option C : Lancer avec Docker**
```bash
# Une fois Docker installé
docker-compose up -d

# Attendre 1 minute que Kafka démarre
docker ps

# Vérifier que 3 services tournent :
# - bibliotheque-zookeeper
# - bibliotheque-kafka  
# - bibliotheque-backend
```

---

## ☁️ SOLUTION CLOUD : Utiliser Render (SANS RIEN INSTALLER)

**L'app est déjà en ligne !**
```
https://gestion-bibliotheque-esiea-final.onrender.com/swagger-ui.html
```

**Avantages** :
- ✅ Rien à installer
- ✅ Fonctionne immédiatement
- ✅ PostgreSQL avec données persistées
- ⚠️ Kafka désactivé (mais app fonctionne)

---

## 📝 RÉSUMÉ POUR BHARGAVI

| Méthode | Kafka ? | Installation ? | Temps |
|---------|---------|----------------|-------|
| **./mvnw spring-boot:run** | ❌ Non | Aucune | 30 sec |
| **docker-compose up** | ✅ Oui | Docker requis | 2 min |
| **Render Cloud** | ❌ Non | Aucune | 0 sec |

**Mon conseil : Utiliser Render ou mvnw spring-boot:run**
