package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.exception.UserNotFoundException;
import com.aaronr92.tanksgame.model.User;
import com.aaronr92.tanksgame.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findOrCreate(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseGet(() -> save(createUser(id)));
    }

    private User createUser(long id) {
        return new User(
                id,
                100,
                4,
                LocalDate.now().minusDays(1)
        );
    }

    public User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public void remove(long id) {
        userRepository.deleteById(id);
    }

    public void updateBoxOpenTime(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (user.getDaysFromLastBoxOpen() < 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You have already opened the box today");

        user.setLastOpenTime(LocalDate.now());
        userRepository.save(user);
    }
}
