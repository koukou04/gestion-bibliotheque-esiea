package com.bibliotheque.gestion_bibliotheque.application.mapper;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Reservation;
import com.bibliotheque.gestion_bibliotheque.application.dto.ReservationDto;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationMapper {

    public static ReservationDto toDto(Reservation reservation) {
        if (reservation == null) return null;

        return new ReservationDto(
                reservation.getId(),
                reservation.getLivreId(),
                reservation.getMembreId(),
                reservation.getDateReservation(),
                reservation.getStatut(),
                reservation.getPosition(),
                reservation.getDateExpiration()
        );
    }

    public static Reservation toEntity(ReservationDto dto) {
        if (dto == null) return null;

        return new Reservation(
                dto.getId(),
                dto.getLivreId(),
                dto.getMembreId(),
                dto.getDateReservation(),
                dto.getPosition()
        );
    }

    public static List<ReservationDto> toDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
    }
}
