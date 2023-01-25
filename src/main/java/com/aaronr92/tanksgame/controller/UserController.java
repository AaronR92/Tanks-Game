package com.aaronr92.tanksgame.controller;

import com.aaronr92.tanksgame.model.User;
import com.aaronr92.tanksgame.service.UserService;
import com.aaronr92.tanksgame.util.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String tankName,
            @RequestParam Operation operation
    ) {
        return ResponseEntity.ok(userService.updateUser(id, tankName, operation));
    }
}
