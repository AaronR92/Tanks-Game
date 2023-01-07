package com.aaronr92.tanksgame;

import com.aaronr92.tanksgame.model.Tank;
import com.aaronr92.tanksgame.service.TankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TanksGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(TanksGameApplication.class, args);
    }

    @Bean
    CommandLineRunner addItems(TankService tankService) {
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
            vipera.setPrice(5000);
            vipera.setLevel(8);

            Tank ambt = new Tank(
                    "AMBT",
                    5500,
                    "Магазин с системой дозарядки на 3 снаряда",
                    Tank.Class.MEDIUM,
                    Tank.Type.PREMIUM,
                    Tank.Nation.USA,
                    8
            );
            ambt.setId(2);

            Tank su_2_122 = new Tank(
                    "СУ-2-122",
                    3100,
                    "Обладает механикой двуствольных орудий",
                    Tank.Class.MEDIUM,
                    Tank.Type.PREMIUM,
                    Tank.Nation.USSR,
                    5
            );
            su_2_122.setId(3);

            Tank bz176 = new Tank(
                    "BZ-176",
                    8500,
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
}
