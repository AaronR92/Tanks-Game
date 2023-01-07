package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.model.Tank;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class TankService {
    private final Map<Long, Tank> tanks = new HashMap<>();

    public void save(Tank tank) {
        tanks.put(tank.getId(), tank);
    }

    public Tank findItemById(long id) {
        return tanks.get(id);
    }

    public void delete(long id) {
        tanks.remove(id);
    }

    public Collection<Tank> findAll() {
        return tanks.values();
    }
}
