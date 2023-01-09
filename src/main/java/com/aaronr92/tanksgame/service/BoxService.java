package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.model.User;
import com.aaronr92.tanksgame.util.RewardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BoxService {
    private final Logger log = LoggerFactory.getLogger(BoxService.class);
    private final Random random = new Random();
    private final TankService tankService;
    private final UserService userService;

    public BoxService(TankService tankService, UserService userService) {
        this.tankService = tankService;
        this.userService = userService;
    }

    public RewardResponse openBox(long id) {
        User user = userService.findOrCreate(id);
        Object reward = getReward(id);
        RewardResponse response = new RewardResponse();

        if (reward instanceof Tank tank) {
            if (user.getTanks().contains(tank)) {
                float tankPrice = tank.getPrice();
                user.addMoney(tankPrice);
                response.setReward(String.valueOf(tankPrice));
                response.setTank(tank.getName());
            } else {
                user.addTank(tank);
                response.setReward(" " + tank.getName());
            }
        } else {
            float money = (float) reward;
            user.addMoney(money);
            response.setReward(String.valueOf(money));
        }

        userService.save(user);
        return response;
    }

    private Object getReward(long id) {
        userService.updateBoxOpenTime(id);

        float chance = random.nextFloat();

        if (chance <= 0.05) {
            Tank tank = getTank();
            log.info("Tank {} has dropped for {}!", tank.getName(), id);
            return tank;
        } else if (chance <= 0.1) {
            return 500f;
        } else if (chance <= 0.3) {
            return 250f;
        } else if (chance <= 0.6) {
            return 150f;
        } else {
            return 100f;
        }
    }

    private Tank getTank() {
        return tankService.findTankById(random.nextInt(3) + 1);
    }
}
