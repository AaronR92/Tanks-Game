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
}
