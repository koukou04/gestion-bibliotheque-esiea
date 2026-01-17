package com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer;


import com.bibliotheque.gestion_bibliotheque.adapters.messaging.event.ReservationCreeEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventProducer {

    private final KafkaTemplate<String, ReservationCreeEvent> kafkaTemplate;
    private static final String TOPIC = "livre-reserve";

    public ReservationEventProducer(KafkaTemplate<String, ReservationCreeEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publierReservation(ReservationCreeEvent event) {
        kafkaTemplate.send(TOPIC, event);
        System.out.println("✅ Événement publié sur le topic: " + TOPIC);
        System.out.println("📚 Livre réservé: " + event.getTitreLivre());
    }
}