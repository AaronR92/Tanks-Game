package com.aaronr92.tanksgame.model;

import com.aaronr92.tanksgame.exception.InsufficientFundsException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_user")
public class User {

    @Id
    private long id;

    private int money;

    @JsonIgnore
    private LocalDate lastOpenTime;

    @JsonManagedReference
    @ManyToMany
    private Set<Tank> tanks;

    private int maxHangarSize;

    public User() { }

    public User(long id, int money, int maxHangarSize, LocalDate lastOpenTime) {
        this.id = id;
        this.money = money;
        this.maxHangarSize = maxHangarSize;
        this.lastOpenTime = lastOpenTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public LocalDate getLastOpenTime() {
        return lastOpenTime;
    }

    public void setLastOpenTime(LocalDate lastOpenTime) {
        this.lastOpenTime = lastOpenTime;
    }

    public Set<Tank> getTanks() {
        checkSet();
        return tanks;
    }

    public void setTanks(Set<Tank> tanks) {
        this.tanks = tanks;
    }

    public void addTank(Tank tank) {
        checkSet();
        tanks.add(tank);
    }

    public void removeTank(Tank tank) {
        checkSet();
        tanks.remove(tank);
    }

    public void addMoney(int value) {
        this.money += value;
    }

    public void subtractMoney(int value) {
        this.money -= value;
    }

    public int getMaxHangarSize() {
        return maxHangarSize;
    }

    public void setMaxHangarSize(int maxHangarSize) {
        this.maxHangarSize = maxHangarSize;
    }

    public void increaseMaxHangarSize(int value) {
        this.maxHangarSize += value;
    }

    public void decreaseMaxHangarSize(int value) {
        this.maxHangarSize -= value;
    }

    public int getDaysFromLastBoxOpen() {
        return Period.between(lastOpenTime, LocalDate.now()).getDays();
    }

    private void checkSet() {
        if (tanks == null) {
            tanks = new HashSet<>();
        }

        if (tanks.size() == maxHangarSize) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Your hangar is full"
            );
        }
    }


}
