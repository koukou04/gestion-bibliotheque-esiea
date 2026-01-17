package com.bibliotheque.gestion_bibliotheque.adapters.repository;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Reservation;
import com.bibliotheque.gestion_bibliotheque.domain.repository.ReservationRepository;
import com.bibliotheque.gestion_bibliotheque.adapters.infrastructure.entity.ReservationEntity;
import com.bibliotheque.gestion_bibliotheque.adapters.infrastructure.repository.JpaReservationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final JpaReservationRepository jpaRepository;

    public ReservationRepositoryAdapter(JpaReservationRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity entity = toEntity(reservation);
        ReservationEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Reservation> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByMembreId(Long membreId) {
        return jpaRepository.findByMembreId(membreId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByLivreId(Long livreId) {
        return jpaRepository.findByLivreId(livreId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByLivreIdAndStatutOrderByPosition(Long livreId, String statut) {
        return jpaRepository.findByLivreIdAndStatutOrderByPosition(livreId, statut).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByStatut(String statut) {
        return jpaRepository.findByStatut(statut).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int countByLivreIdAndStatut(Long livreId, String statut) {
        return jpaRepository.countByLivreIdAndStatut(livreId, statut);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    // ========== MAPPERS PRIVÉS ==========

    private ReservationEntity toEntity(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setId(reservation.getId());
        entity.setLivreId(reservation.getLivreId());
        entity.setMembreId(reservation.getMembreId());
        entity.setDateReservation(reservation.getDateReservation());
        entity.setStatut(reservation.getStatut());
        entity.setPosition(reservation.getPosition());
        entity.setDateExpiration(reservation.getDateExpiration());
        return entity;
    }

    private Reservation toDomain(ReservationEntity entity) {
        Reservation reservation = new Reservation();
        reservation.setId(entity.getId());
        reservation.setLivreId(entity.getLivreId());
        reservation.setMembreId(entity.getMembreId());
        reservation.setDateReservation(entity.getDateReservation());
        reservation.setStatut(entity.getStatut());
        reservation.setPosition(entity.getPosition());
        reservation.setDateExpiration(entity.getDateExpiration());
        return reservation;
    }
}
