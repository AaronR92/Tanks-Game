package com.aaronr92.tanksgame.repository;

import com.aaronr92.tanksgame.model.Expedition;
import com.aaronr92.tanksgame.model.Tank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ExpeditionRepository extends JpaRepository<Expedition, Long> {
    Optional<Expedition> findExpeditionByUser_IdAndFinishedFalse(Long id);

    Collection<Expedition> findExpeditionsByFinishedFalse();

    boolean existsByUser_IdAndFinishedFalse(Long userId);

    boolean existsByUser_IdAndTank_IdAndFinishedFalse(Long userId, Long tankId);

    Page<Expedition> findExpeditionsByUser_Id(long userId, Pageable pageable);
}