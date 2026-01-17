package com.bibliotheque.gestion_bibliotheque.adapters.infrastructure.repository;

import com.bibliotheque.gestion_bibliotheque.adapters.infrastructure.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByMembreId(Long membreId);

    List<ReservationEntity> findByLivreId(Long livreId);

    List<ReservationEntity> findByLivreIdAndStatutOrderByPosition(Long livreId, String statut);

    List<ReservationEntity> findByStatut(String statut);

    int countByLivreIdAndStatut(Long livreId, String statut);
}
