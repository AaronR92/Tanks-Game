package com.aaronr92.tanksgame.controller;

import com.aaronr92.tanksgame.model.Expedition;
import com.aaronr92.tanksgame.model.User;
import com.aaronr92.tanksgame.service.ExpeditionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/expedition")
public class ExpeditionController {

    private final ExpeditionService expeditionService;

    public ExpeditionController(ExpeditionService expeditionService) {
        this.expeditionService = expeditionService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Expedition> getExpeditionByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(expeditionService.findByUserId(userId));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Expedition> getExpeditionById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(expeditionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Expedition> createExpedition(
            @RequestParam Long userId,
            @RequestParam Long tankId
    ) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/expedition")
                .toUriString());

        return ResponseEntity.created(uri)
                .body(expeditionService.save(userId, tankId));
    }
}
