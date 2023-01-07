package com.aaronr92.tanksgame.service;

import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.model.User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BoxService {
    private final Random random = new Random();
    private final TankService tankService;
    private final UserService userService;

    public BoxService(TankService tankService, UserService userService) {
        this.tankService = tankService;
        this.userService = userService;
    }

    public Object openBox(long id) {
        Object reward = calculateChances(id);

        User user = userService.findUserById(id);

        if (reward instanceof Tank tank) {
            if (user.getTanks().contains(tank)) {
                float tankPrice = tank.getPrice();
                user.addMoney(tankPrice);
                return tankPrice;
            } else {
                user.addTank(tank);
                return tank;
            }
        } else {
            float moneyReward = (float) reward;
            user.addMoney(moneyReward);
            return moneyReward;
        }
    }

    private Object calculateChances(long id) {
        userService.updateBoxOpenTime(id);

        float chance = random.nextFloat();

        if (chance <= 0.05) {
            return getTank();
        } else if (chance <= 0.1) {
            return 400f;
        } else if (chance <= 0.3) {
            return 200f;
        } else if (chance <= 0.6) {
            return 100f;
        } else {
            return 0f;
        }
    }

    private Tank getTank() {
        return tankService.findItemById(random.nextInt(3));
    }
}
