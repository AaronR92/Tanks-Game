package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public User findUserById(long id) {
        if (!users.containsKey(id)) {
            new User(
                    id,
                    100,
                    LocalDate.now().minusDays(1)
            );
        }
        return users.get(id);
    }

    public void remove(long id) {
        users.remove(id);
    }

    public void updateBoxOpenTime(long id) throws IllegalArgumentException {
        User user = users.get(id);

        if (user.getDaysFromLastBoxOpen() < 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You have already opened the box today");

        user.setLastOpenTime(LocalDate.now());
    }
}
