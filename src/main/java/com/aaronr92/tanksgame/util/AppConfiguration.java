package com.aaronr92.tanksgame.util;

import com.aaronr92.tanksgame.model.Expedition;
import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.repository.ExpeditionRepository;
import com.aaronr92.tanksgame.service.ExpeditionService;
import com.aaronr92.tanksgame.service.TankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Collection;

@Configuration
public class AppConfiguration {

    @Bean
    public ThreadPoolTaskScheduler getTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(2);
        taskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return taskScheduler;
    }

    @Bean
    public CommandLineRunner fillDb(TankService tankService) {
        return args -> {
            if (tankService.findAll().size() != 0)
                return;

            Tank vipera = new Tank();
            vipera.setId(1);
            vipera.setName("Vipera");
            vipera.setDescription("Имеет магазин на 5 снарядов");
            vipera.setNation(Tank.Nation.ITALY);
            vipera.setType(Tank.Type.PREMIUM);
            vipera.setTankClass(Tank.Class.TANK_DESTROYER);
            vipera.setPrice(11500);
            vipera.setLevel(8);

            Tank ambt = new Tank(
                    "AMBT",
                    11000,
                    "Магазин с системой дозарядки на 3 снаряда",
                    Tank.Class.MEDIUM,
                    Tank.Type.PREMIUM,
                    Tank.Nation.USA,
                    8
            );
            ambt.setId(2);

            Tank su_2_122 = new Tank(
                    "СУ-2-122",
                    6200,
                    "Обладает механикой двуствольных орудий",
                    Tank.Class.TANK_DESTROYER,
                    Tank.Type.PREMIUM,
                    Tank.Nation.USSR,
                    5
            );
            su_2_122.setId(3);

            Tank bz176 = new Tank(
                    "BZ-176",
                    13000,
                    "Имеет ракетные ускорители",
                    Tank.Class.HEAVY,
                    Tank.Type.PREMIUM,
                    Tank.Nation.CHINA,
                    8
            );
            bz176.setId(4);

            tankService.save(vipera);
            tankService.save(ambt);
            tankService.save(su_2_122);
            tankService.save(bz176);
        };
    }

    @Bean
    CommandLineRunner runNotFinishedTasks(
            ExpeditionRepository repository,
            ExpeditionService service,
            ThreadPoolTaskScheduler taskScheduler
    ) {
        return args -> {
            Collection<Expedition> expeditions = repository
                    .findExpeditionsByFinishedFalse();

            expeditions.forEach(expedition -> taskScheduler.schedule(() -> {
                expedition.setFinished(true);
                int reward = service.giveRewards(
                        expedition.getUser(),
                        expedition.getTank(),
                        expedition.getPeriod()
                );
                expedition.setReward(reward);
                repository.save(expedition);
            }, expedition.getFinishTime()));
        };
    }
}
