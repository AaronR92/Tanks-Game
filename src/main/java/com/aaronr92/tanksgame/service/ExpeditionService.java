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

    public Expedition save(Long userId, Long tankId) {
        checkUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Tank tank = tankRepository.findById(tankId)
                .orElseThrow(TankNotFoundException::new);

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

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        expeditionRepository.deleteById(id);
    }

    public Expedition findById(Long id) {
        return expeditionRepository.findById(id)
                .orElseThrow(ExpeditionNotFoundException::new);
    }

    public Expedition findByUserId(Long userId) {
        return expeditionRepository
                .findExpeditionByUser_IdAndFinishedFalse(userId)
                .orElseThrow(ExpeditionNotFoundException::new);
    }

    private void checkUser(long userId) {
        if (expeditionRepository.existsByUser_IdAndFinishedFalse(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User already in expedition");
        }
    }

    public void giveRewards(User user, Tank tank, Expedition.Period expeditionPeriod) {
        int period = expeditionPeriod.getHours();
        int level = tank.getLevel();

        int periodReward = 10 * period;

        float levelReward = periodReward + (4 * periodReward * Float.parseFloat(level == 10 ? "1f" : "0." + level));

        float rawReward = levelReward * 1.5f;

        float randAdd = random.nextFloat(0.1f);

        float additive = rawReward * randAdd;

        int reward = (int) (rawReward + additive);

        user.addMoney(reward);
        save(user);
    }
}
