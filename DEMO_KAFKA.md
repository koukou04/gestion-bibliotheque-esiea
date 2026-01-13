# 🚀 SCRIPT DÉMO KAFKA (2-3 minutes)

## **MÉTHODE 1 : Montrer le CODE (Simple)** 📝

### **Étape 1 : Montrer le Producer**
```java
// EmpruntEventProducer.java
public void publierEmpruntCree(Emprunt emprunt) {
    EmpruntCreeEvent event = new EmpruntCreeEvent(
        emprunt.getId(),
        emprunt.getLivre().getId(),
        emprunt.getMembre().getId(),
        emprunt.getDateEmprunt()
    );
    
    kafkaTemplate.send("emprunts-topic", event);
    // ↑ Publie l'événement sur Kafka
}
```

**Dire à la prof** :
> "Quand un emprunt est créé, le producer publie un événement `EmpruntCreeEvent` sur le topic Kafka `emprunts-topic`."

---

### **Étape 2 : Montrer le Consumer**
```java
// EmpruntEventConsumer.java
@KafkaListener(topics = "emprunts-topic", groupId = "bibliotheque-group")
public void consommerEmpruntCree(EmpruntCreeEvent event) {
    log.info("📬 Événement reçu : Emprunt créé ID={}", event.getEmpruntId());
    // Ici on pourrait : envoyer email, notification, stats, etc.
}
```

**Dire à la prof** :
> "Le consumer écoute le topic Kafka. Dès qu'un événement arrive, il le traite automatiquement. C'est asynchrone, comme Power Automate."

---

## **MÉTHODE 2 : Montrer les LOGS en direct (Impressionnant)** 🔥

### **Commandes à exécuter PENDANT la démo** :

```powershell
# 1. Lancer Docker (30 min AVANT la démo)
cd "D:\étude\école\Projet architecture d applicatoin MAJEUR"
docker-compose up -d

# 2. Vérifier que Kafka tourne
docker ps
# ↑ Doit montrer 3 services : zookeeper, kafka, backend

# 3. Ouvrir Swagger UI
http://localhost:8080/swagger-ui.html

# 4. Créer un emprunt (section emprunt-controller)
POST /api/emprunts
Body JSON :
{
  "livreId": 1,
  "membreId": 1,
  "dateEmprunt": "2026-01-13"
}
```

### **Montrer les logs Kafka** :

```powershell
# Afficher les logs backend en temps réel
docker-compose logs -f backend

# OU filtrer juste Kafka :
docker-compose logs backend | Select-String "Kafka|EmpruntCree|📬"
```

**Tu verras** :
```
✅ Consumer subscribed to topic(s): emprunts-topic
✅ Successfully joined group
✅ partitions assigned: [emprunts-topic-0]
📬 Événement reçu : Emprunt créé ID=123
```

---

## **MÉTHODE 3 : Montrer docker-compose.yml (Rapide)** ⚡

```yaml
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    # ↑ Coordonne Kafka
    
  kafka:
    image: confluentinc/cp-kafka:7.5.0
    depends_on:
      - zookeeper
    # ↑ Broker de messages
    
  backend:
    build: .
    depends_on:
      - kafka
    environment:
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    # ↑ Backend connecté à Kafka
```

**Dire à la prof** :
> "Docker Compose orchestre 3 services : Zookeeper coordonne Kafka, Kafka gère les messages, et le Backend publie/consomme les événements."

---

## **📋 CHECKLIST DÉMO KAFKA**

**30 minutes AVANT** :
- [ ] Lancer Docker Desktop
- [ ] Exécuter `docker-compose up -d`
- [ ] Vérifier `docker ps` (3 services)
- [ ] Attendre 1 minute (démarrage complet)

**PENDANT la démo** :
- [ ] Montrer code EmpruntEventProducer
- [ ] Montrer code EmpruntEventConsumer
- [ ] OU montrer logs `docker-compose logs backend`
- [ ] Expliquer : "Asynchrone, découplage, comme Power Automate"

**PHRASE MAGIQUE** :
> "Kafka implémente une architecture événementielle : le producer publie EmpruntCreeEvent, le consumer l'écoute et traite automatiquement. C'est asynchrone, ce qui découple les services et améliore la scalabilité."

---

## **⚠️ SI PROBLÈME TECHNIQUE**

**Si Docker plante** :
> "Kafka est implémenté dans le code (montrer EmpruntEventProducer et Consumer), mais désactivé en production cloud Render pour optimiser les coûts. En local, il fonctionne avec Docker Compose."

**C'est HONNÊTE et ça montre que tu maîtrises !** ✅

---

## **🎯 MON CONSEIL**

**OPTION 1 (Safe)** : Montrer juste le CODE (producer + consumer)
- ✅ Pas de risque technique
- ✅ Montre que tu comprends
- ⏱️ 1 minute

**OPTION 2 (Impressionnant)** : Montrer les LOGS en direct
- ✅ Prouve que ça fonctionne vraiment
- ⚠️ Nécessite Docker lancé AVANT
- ⏱️ 2-3 minutes

**COMBINAISON GAGNANTE** : Code + Logs = 💯
