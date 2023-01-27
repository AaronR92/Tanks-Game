package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.exception.InsufficientFundsException;
import com.aaronr92.tanksgame.exception.OperationNotFoundException;
import com.aaronr92.tanksgame.exception.TankNotFoundException;
import com.aaronr92.tanksgame.exception.UserNotFoundException;
import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.model.User;
import com.aaronr92.tanksgame.repository.ExpeditionRepository;
import com.aaronr92.tanksgame.repository.TankRepository;
import com.aaronr92.tanksgame.repository.UserRepository;
import com.aaronr92.tanksgame.util.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final Logger log;
    private final UserRepository userRepository;
    private final TankRepository tankRepository;
    private final ExpeditionRepository expeditionRepository;
    private final int SLOT_PRICE = 1250;

    public UserService(UserRepository userRepository, TankRepository tankRepository,
                       ExpeditionRepository expeditionRepository) {
        this.userRepository = userRepository;
        this.tankRepository = tankRepository;
        this.expeditionRepository = expeditionRepository;
        this.log = LoggerFactory.getLogger(UserService.class);
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
    public void updateBoxOpenTime(long id)
            throws ResponseStatusException, UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (user.getDaysFromLastBoxOpen() < 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You have already opened the box today");

        user.setLastOpenTime(LocalDate.now());
        userRepository.save(user);
    }

    public User updateUser(Long userId, String tankName, Operation operation) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (operation) {
            case BUY -> buyTank(user, findTankByName(tankName));
            case SELL -> sellTank(user, findTankByName(tankName));
            case SLOT -> buySlot(user);
            default -> throw new OperationNotFoundException();
        }

        return userRepository.save(user);
    }

    private void buySlot(User user) {
        user.subtractMoney(SLOT_PRICE);
        user.increaseMaxHangarSize(1);
        log.info("{} has increased their slot count by one", user.getId());
    }

    private void buyTank(User user, Tank tank) {
        int price = tank.getPrice();
        if (user.getTanks().contains(tank))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You already have this tank");
        if (user.getMoney() < price)
            throw new InsufficientFundsException();
        log.info("{} bought the tank [{}] {}", user.getId(), tank.getId(), tank.getName());

        user.subtractMoney(price);
        user.addTank(tank);
    }

    private void sellTank(User user, Tank tank) {
        if (!user.getTanks().contains(tank))
            throw new TankNotFoundException();
        if (expeditionRepository.existsByUser_IdAndTank_IdAndFinishedFalse(
                user.getId(),
                tank.getId()
        ))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Tank already in expedition");

        log.info("{} sold the tank [{}] {}", user.getId(), tank.getId(), tank.getName());

        user.removeTank(tank);
        user.addMoney(tank.getPrice() / 2);
    }

    private Tank findTankByName(String tankName) {
        return tankRepository.findTankByName(tankName).orElseThrow(TankNotFoundException::new);
    }
}
