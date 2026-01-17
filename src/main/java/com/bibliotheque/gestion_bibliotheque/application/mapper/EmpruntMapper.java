package com.bibliotheque.gestion_bibliotheque.application.mapper;

import com.bibliotheque.gestion_bibliotheque.domain.entities.Emprunt;
import com.bibliotheque.gestion_bibliotheque.application.dto.EmpruntDto;

import java.util.List;
import java.util.stream.Collectors;

public class EmpruntMapper {

    public static EmpruntDto toDto(Emprunt emprunt) {
        if (emprunt == null) return null;

        return new EmpruntDto(
                emprunt.getId(),
                emprunt.getLivreId(),
                emprunt.getMembreId(),
                emprunt.getDateEmprunt(),
                emprunt.getDateRetourPrevue(),
                emprunt.getDateRetourEffective(),
                emprunt.getStatut(),
                emprunt.getPenalite()
        );
    }

    public static Emprunt toEntity(EmpruntDto dto) {
        if (dto == null) return null;

        return new Emprunt(
                dto.getId(),
                dto.getLivreId(),
                dto.getMembreId(),
                dto.getDateEmprunt(),
                dto.getDateRetourPrevue()
        );
    }

    public static List<EmpruntDto> toDtoList(List<Emprunt> emprunts) {
        return emprunts.stream()
                .map(EmpruntMapper::toDto)
                .collect(Collectors.toList());
    }
}
