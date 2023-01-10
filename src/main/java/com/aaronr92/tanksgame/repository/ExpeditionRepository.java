package com.aaronr92.tanksgame.repository;

import com.aaronr92.tanksgame.model.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ExpeditionRepository extends JpaRepository<Expedition, Long> {
    Optional<Expedition> findExpeditionByUser_IdAndFinishedFalse(Long id);

    Collection<Expedition> findExpeditionsByFinishedFalse();

    Boolean existsByUser_IdAndFinishedFalse(Long userId);
}