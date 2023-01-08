package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.repository.TankRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class TankService {
    private final TankRepository tankRepository;

    public TankService(TankRepository tankRepository) {
        this.tankRepository = tankRepository;
    }

    public Tank save(Tank tank) {
        return tankRepository.save(tank);
    }

    public Tank findItemById(long id) {
        return tankRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tank not found"
        ));
    }

    public void delete(long id) {
        tankRepository.deleteById(id);
    }

    public Collection<Tank> findAll() {
        return tankRepository.findAll();
    }
}
