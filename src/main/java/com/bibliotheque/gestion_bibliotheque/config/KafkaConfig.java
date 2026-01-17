package com.bibliotheque.gestion_bibliotheque.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration Kafka pour créer automatiquement les topics nécessaires.
 * 
 * NOTE: Pour que Kafka fonctionne, vous devez avoir un serveur Kafka en cours d'exécution
 * sur localhost:9092. Si Kafka n'est pas disponible, l'application démarrera quand même
 * mais la fonctionnalité événementielle ne sera pas active.
 * 
 * Pour installer et démarrer Kafka:
 * - Téléchargez Kafka depuis https://kafka.apache.org/downloads
 * - Démarrez Zookeeper: bin/zookeeper-server-start.sh config/zookeeper.properties
 * - Démarrez Kafka: bin/kafka-server-start.sh config/server.properties
 * 
 * Pour désactiver Kafka, ajoutez dans application.properties:
 * spring.kafka.enabled=false
 */
@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConfig {

    /**
     * Crée automatiquement le topic "emprunts-topic" s'il n'existe pas
     */
    @Bean
    public NewTopic empruntsTopic() {
        return TopicBuilder.name("emprunts-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
