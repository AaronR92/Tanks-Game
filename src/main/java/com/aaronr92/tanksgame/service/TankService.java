package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.exception.TankNotFoundException;
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

    /**
     * Saves tank
     * @param tank a tank to save
     * @return saved tank
     */
    public Tank save(Tank tank) {
        return tankRepository.save(tank);
    }

    /**
     * Finds a tank by id
     * @param id an id of a tank to find
     * @return found tank
     * @throws ResponseStatusException if tank is not present
     */
    public Tank findTankById(long id) throws ResponseStatusException {
        return tankRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tank not found"
        ));
    }

    /**
     * Removes a tank from the database
     * @param id an id of a tank to remove
     */
    public void delete(long id) {
        tankRepository.deleteById(id);
    }

    /**
     * Finds all tanks in database
     * @return found tanks
     */
    public Collection<Tank> findAll() {
        return tankRepository.findAll();
    }

    /**
     * Finds a tank by its name
     * @param name a name of a tank to find
     * @return found tank
     * @throws TankNotFoundException if a tank is not present
     */
    public Tank findTankByName(String name) throws TankNotFoundException {
        return tankRepository.findTankByName(name)
                .orElseThrow(TankNotFoundException::new);
    }
}
