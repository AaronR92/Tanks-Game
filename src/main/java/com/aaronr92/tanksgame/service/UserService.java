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

    /**
     * Saves user
     * @param user a user
     * @return saved user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds or creates new user if it is not present
     * @param id an id of a new user
     * @return created user
     */
    public User findOrCreate(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseGet(() -> save(createUser(id)));
    }

    /**
     * Creates new user
     * @param id an id of a new user
     * @return created user
     */
    private User createUser(long id) {
        return new User(
                id,
                100,
                4,
                LocalDate.now().minusDays(1)
        );
    }

    /**
     * Finds a user by id
     * @param id an id to find
     * @return found user
     * @throws UserNotFoundException if user is not present
     */
    public User findUserById(long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Removes a user from the database
     * @param id an id of a user to remove
     */
    public void remove(long id) {
        userRepository.deleteById(id);
    }

    /**
     * Update the last box open time
     * @param id an id of a user to update
     * @throws ResponseStatusException if a user has already opened the box today
     * @throws UserNotFoundException if user is not present
     */
    public void updateBoxOpenTime(long id) throws ResponseStatusException, UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (user.getDaysFromLastBoxOpen() < 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You have already opened the box today");

        user.setLastOpenTime(LocalDate.now());
        userRepository.save(user);
    }
}
