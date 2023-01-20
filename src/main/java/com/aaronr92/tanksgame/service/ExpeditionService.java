package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.exception.ExpeditionNotFoundException;
import com.aaronr92.tanksgame.exception.TankNotFoundException;
import com.aaronr92.tanksgame.exception.UserNotFoundException;
import com.aaronr92.tanksgame.model.Expedition;
import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.model.User;
import com.aaronr92.tanksgame.repository.ExpeditionRepository;
import com.aaronr92.tanksgame.repository.TankRepository;
import com.aaronr92.tanksgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Service
public class ExpeditionService {

    private final ExpeditionRepository expeditionRepository;
    private final UserRepository userRepository;
    private final TankRepository tankRepository;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final Random random;

    @Autowired
    public ExpeditionService(ExpeditionRepository expeditionRepository,
                             UserRepository userRepository,
                             TankRepository tankRepository,
                             ThreadPoolTaskScheduler taskScheduler) {
        this.expeditionRepository = expeditionRepository;
        this.userRepository = userRepository;
        this.tankRepository = tankRepository;
        this.taskScheduler = taskScheduler;
        this.random = new Random();
    }

    /**
     * Saves new expedition
     * @param userId an id of a user
     * @param tankName a name of a tank
     * @return saved expedition
     * @throws ResponseStatusException if user does not have this tank
     */
    public Expedition save(Long userId, String tankName) throws ResponseStatusException {
        checkUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Tank tank = tankRepository.findTankByName(tankName)
                .orElseThrow(TankNotFoundException::new);

        if (!user.getTanks().contains(tank))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User does not have this tank");

        Expedition expedition = new Expedition();
        expedition.setUser(user);
        expedition.setTank(tank);
        expedition.setFinished(false);
        Expedition.Period period = Expedition.Period.random();
        expedition.setPeriod(period);

        expeditionRepository.saveAndFlush(expedition);

        taskScheduler.schedule(() -> {
            expedition.setFinished(true);
            giveRewards(user, tank, period);
            expeditionRepository.save(expedition);
        }, expedition.getFinishTime());

        return expedition;
    }

    /**
     * Saves user
     * @param user user to save
     * @return saved user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Removes expedition from database
     * @param id an id of an expedition
     */
    public void delete(Long id) {
        expeditionRepository.deleteById(id);
    }

    /**
     * Finds expedition by id
     * @param id an id of an expedition
     * @return found expedition
     */
    public Expedition findById(Long id) {
        return expeditionRepository.findById(id)
                .orElseThrow(ExpeditionNotFoundException::new);
    }

    /**
     * Finds expedition by user id
     * @param userId an id of a user
     * @return found expedition
     */
    public Expedition findByUserId(Long userId) {
        return expeditionRepository
                .findExpeditionByUser_IdAndFinishedFalse(userId)
                .orElseThrow(ExpeditionNotFoundException::new);
    }

    /**
     * Validates user
     * @param userId an id of a user
     */
    private void checkUser(long userId) {
        if (expeditionRepository.existsByUser_IdAndFinishedFalse(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User already in expedition");
        }
    }

    /**
     * Gives rewards to a user for expedition
     * @param user a user
     * @param tank a tank
     * @param expeditionPeriod period of an expedition
     */
    public void giveRewards(User user, Tank tank, Expedition.Period expeditionPeriod) {
        int period = expeditionPeriod.getHours();
        int level = tank.getLevel();

        int periodReward = 3 * period;

        float levelReward = periodReward + (15 * periodReward *
                Float.parseFloat(level == 10 ? "1f" : "0." + level));

        float rawReward = levelReward * 1.65f;

        float randAdd = random.nextFloat(0.1f);

        float additive = rawReward * randAdd;

        int reward = (int) (rawReward + additive);

        user.addMoney(reward);
        save(user);
    }

    public Page<Expedition> getExpeditionLogPage(long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("startTime").descending());
        return expeditionRepository.findExpeditionsByUser_Id(userId, pageable);
    }
}
