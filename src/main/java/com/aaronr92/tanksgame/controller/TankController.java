package com.aaronr92.tanksgame.controller;

import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.service.TankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tank")
public class TankController {

    private final TankService tankService;

    public TankController(TankService tankService) {
        this.tankService = tankService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Tank> getTankById(@PathVariable long id) {
        return ResponseEntity.ok(tankService.findTankById(id));
    }

    @GetMapping
    public ResponseEntity<Tank> getTankByName(@RequestParam String name) {
        return ResponseEntity.ok(tankService.findTankByName(name));
    }
}
