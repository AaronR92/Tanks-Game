package com.aaronr92.tanksgame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {

    private long id;

    private float money;
//    @Field("last_open_time")
    @JsonIgnore
    private LocalDate lastOpenTime;
    private Set<Tank> tanks;

    @Transient
    private int daysFromLastBoxOpen;

    public User() {}

    public User(long id, float money, LocalDate lastOpenTime) {
        this.id = id;
        this.money = money;
        this.lastOpenTime = lastOpenTime;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public float getMoney() {
        return money;
    }

    public User setMoney(float money) {
        this.money = money;
        return this;
    }

    public LocalDate getLastOpenTime() {
        return lastOpenTime;
    }

    public User setLastOpenTime(LocalDate lastOpenTime) {
        this.lastOpenTime = lastOpenTime;
        return this;
    }

    public Set<Tank> getTanks() {
        checkSet();
        return tanks;
    }

    public User addTank(Tank tank) {
        checkSet();
        tanks.add(tank);
        return this;
    }

    public User addMoney(float value) {
        this.money += value;
        return this;
    }

    private void checkSet() {
        if (tanks == null) {
            tanks = new HashSet<>();
        }
    }

    public int getDaysFromLastBoxOpen() {
        return Period.between(lastOpenTime, LocalDate.now()).getDays();
    }
}
