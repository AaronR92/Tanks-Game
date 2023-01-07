package com.aaronr92.tanksgame.controller;

import com.aaronr92.tanksgame.service.BoxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/box")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> openBox(@PathVariable long id) {
        return ResponseEntity.ok(boxService.openBox(id));
    }
}
