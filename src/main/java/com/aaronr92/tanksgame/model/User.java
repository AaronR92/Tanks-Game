package com.aaronr92.tanksgame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.Transient;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_user")
public class User {

    @Id
    private long id;

    private float money;

    @Column(name = "last_open_time")
    @JsonIgnore
    private LocalDate lastOpenTime;

    @JsonManagedReference
    @ManyToMany
    private Set<Tank> tanks;

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
        System.out.println("setLastOpenTime");
        this.lastOpenTime = lastOpenTime;
        return this;
    }

    public Set<Tank> getTanks() {
        checkSet();
        return tanks;
    }

    public User setTanks(Set<Tank> tanks) {
        this.tanks = tanks;
        return this;
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
