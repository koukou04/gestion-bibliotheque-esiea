package com.bibliotheque.gestion_bibliotheque.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consommateur Kafka pour traiter les événements d'emprunt.
 * Ce composant écoute les messages Kafka et effectue des traitements
 * asynchrones (logs, notifications, statistiques, etc.).
 * NOTE: Ce composant est désactivé si Kafka n'est pas configuré (spring.kafka.enabled=false)
 */
@Component
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class EmpruntEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EmpruntEventConsumer.class);
    private final ObjectMapper objectMapper;

    public EmpruntEventConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Écoute les événements du topic "emprunts-topic"
     */
    @KafkaListener(topics = "emprunts-topic", groupId = "bibliotheque-group")
    public void consommerEmpruntCree(String message) {
        try {
            logger.info("=== Événement Kafka reçu ===");
            logger.info("Message brut: {}", message);

            // Désérialiser le message
            Map<String, Object> event = objectMapper.readValue(message, Map.class);

            // Traiter l'événement
            logger.info("Traitement de l'événement EmpruntCree:");
            logger.info("  - Emprunt ID: {}", event.get("empruntId"));
            logger.info("  - Livre: {} (ID: {})", event.get("titreLivre"), event.get("livreId"));
            logger.info("  - Membre: {} (ID: {})", event.get("nomMembre"), event.get("membreId"));
            logger.info("  - Date d'emprunt: {}", event.get("dateEmprunt"));
            logger.info("  - Date de retour prévue: {}", event.get("dateRetourPrevu"));

            // Ici, on pourrait :
            // - Envoyer une notification par email au membre
            // - Mettre à jour des statistiques en temps réel
            // - Déclencher d'autres processus métier

            logger.info("✓ Événement traité avec succès");

        } catch (Exception e) {
            logger.error("Erreur lors du traitement de l'événement: {}", e.getMessage(), e);
        }
    }
}
