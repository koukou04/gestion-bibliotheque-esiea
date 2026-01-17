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
public class ReservationEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ReservationEventConsumer.class);
    private final ObjectMapper objectMapper;

    public ReservationEventConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "reservations-topic", groupId = "bibliotheque-group")
    public void consommerReservation(String message) {
        try {
            logger.info("=== Événement Kafka reçu (Réservation) ===");
            logger.info("Message brut: {}", message);

            Map<String, Object> event = objectMapper.readValue(message, Map.class);

            logger.info("Traitement de l'événement ReservationCree:");
            logger.info("  - Réservation ID: {}", event.get("reservationId"));
            logger.info("  - Livre: {} (ID: {})", event.get("titreLivre"), event.get("livreId"));
            logger.info("  - Membre: {} (ID: {})", event.get("nomMembre"), event.get("membreId"));
            logger.info("  - Email: {}", event.get("emailMembre"));
            logger.info("  - Date de réservation: {}", event.get("dateReservation"));
            logger.info("  - Position dans la file: {}", event.get("positionDansFile"));

            logger.info("  → Email de confirmation à envoyer à {}", event.get("emailMembre"));
            logger.info("  → Notification: \"Vous êtes en position {} pour le livre {}\"",
                    event.get("positionDansFile"), event.get("titreLivre"));

            logger.info("✓ Événement traité avec succès");

        } catch (Exception e) {
            logger.error("Erreur lors du traitement de l'événement: {}", e.getMessage(), e);
        }
    }
}