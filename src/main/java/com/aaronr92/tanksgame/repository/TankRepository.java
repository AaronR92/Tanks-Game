package com.aaronr92.tanksgame.repository;

import com.aaronr92.tanksgame.model.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TankRepository extends JpaRepository<Tank, Long> {

}
