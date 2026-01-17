package com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer;

import com.bibliotheque.gestion_bibliotheque.adapters.messaging.event.EmpruntCreeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Producteur Kafka pour publier les événements d'emprunt.
 * Ce composant envoie des messages dans Kafka lorsqu'un emprunt est créé.
 * NOTE: Ce composant est désactivé si Kafka n'est pas configuré (spring.kafka.enabled=false)
 */
@Component
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class EmpruntEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(EmpruntEventProducer.class);
    private static final String TOPIC = "emprunts-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public EmpruntEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Publie un événement EmpruntCreeEvent dans Kafka
     */
    public void publierEmpruntCree(EmpruntCreeEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, String.valueOf(event.getEmpruntId()), message);
            logger.info("Événement EmpruntCree publié dans Kafka: {}", event);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors de la sérialisation de l'événement: {}", e.getMessage());
        }
    }
}
