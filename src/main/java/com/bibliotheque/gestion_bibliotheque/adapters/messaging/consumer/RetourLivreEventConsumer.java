package com.bibliotheque.gestion_bibliotheque.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class RetourLivreEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RetourLivreEventConsumer.class);
    private final ObjectMapper objectMapper;

    public RetourLivreEventConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "livre-retourne", groupId = "bibliotheque-group")
    public void consommerRetourLivre(String message) {
        try {
            logger.info("=== Événement Kafka reçu (Retour de Livre) ===");
            logger.info("Message brut: {}", message);

            Map<String, Object> event = objectMapper.readValue(message, Map.class);

            logger.info("Traitement de l'événement RetourLivre:");
            logger.info("  - Emprunt ID: {}", event.get("empruntId"));
            logger.info("  - Livre: {} (ID: {})", event.get("titreLivre"), event.get("livreId"));
            logger.info("  - Membre: {} (ID: {})", event.get("nomMembre"), event.get("membreId"));
            logger.info("  - Date de retour prévue: {}", event.get("dateRetourPrevue"));
            logger.info("  - Date de retour effective: {}", event.get("dateRetourEffective"));
            logger.info("  - En retard: {}", event.get("enRetard"));

            if (Boolean.TRUE.equals(event.get("enRetard"))) {
                logger.warn("  ⚠️  RETARD DÉTECTÉ ! Pénalité: {}€", event.get("penalite"));
                logger.info("  → Notification d'avertissement à envoyer au membre");
                logger.info("  → Score de fiabilité diminué");
            } else {
                logger.info("  ✓ Retour à temps ! Félicitations au membre");
                logger.info("  → Notification de remerciement à envoyer");
                logger.info("  → Score de fiabilité augmenté");
            }

            logger.info("✓ Événement traité avec succès");

        } catch (Exception e) {
            logger.error("Erreur lors du traitement de l'événement: {}", e.getMessage(), e);
        }
    }
}