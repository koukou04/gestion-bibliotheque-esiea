package com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer;


import com.bibliotheque.gestion_bibliotheque.adapters.messaging.event.RetourLivreEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetourLivreEventProducer {

    private final KafkaTemplate<String, RetourLivreEvent> kafkaTemplate;
    private static final String TOPIC = "livre-retourne";

    public RetourLivreEventProducer(KafkaTemplate<String, RetourLivreEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publierRetourLivre(RetourLivreEvent event) {
        kafkaTemplate.send(TOPIC, event);
        System.out.println("✅ Événement publié sur le topic: " + TOPIC);
        System.out.println("📚 Livre retourné: " + event.getTitreLivre());
    }
}