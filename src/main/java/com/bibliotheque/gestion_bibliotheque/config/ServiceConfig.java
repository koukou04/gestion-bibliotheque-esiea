package com.bibliotheque.gestion_bibliotheque.config;

import com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer.EmpruntEventProducer;
import com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer.ReservationEventProducer;
import com.bibliotheque.gestion_bibliotheque.adapters.messaging.producer.RetourLivreEventProducer;
import com.bibliotheque.gestion_bibliotheque.application.service.*;
import com.bibliotheque.gestion_bibliotheque.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public LivreService livreService(LivreRepository livreRepository) {
        return new LivreService(livreRepository);
    }

    @Bean
    public MembreService membreService(MembreRepository membreRepository) {
        return new MembreService(membreRepository);
    }

    @Bean
    public ReservationService reservationService(
            ReservationRepository reservationRepository,
            LivreService livreService,
            MembreService membreService,
            @Autowired(required = false) ReservationEventProducer reservationEventProducer) {
        return new ReservationService(reservationRepository, livreService, membreService, reservationEventProducer);
    }

    @Bean
    public EmpruntService empruntService(
            EmpruntRepository empruntRepository,
            LivreService livreService,
            MembreService membreService,
            ReservationService reservationService,
            @Autowired(required = false) EmpruntEventProducer empruntEventProducer,
            @Autowired(required = false) RetourLivreEventProducer retourLivreEventProducer) {
        return new EmpruntService(empruntRepository, livreService, membreService, reservationService, empruntEventProducer, retourLivreEventProducer);
    }

    @Bean
    public StatistiqueService statistiqueService(
            EmpruntRepository empruntRepository,
            LivreRepository livreRepository,
            MembreRepository membreRepository) {
        return new StatistiqueService(empruntRepository, livreRepository, membreRepository);
    }
}
