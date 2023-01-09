package com.aaronr92.tanksgame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Entity
public class Expedition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    private User user;
    @OneToOne
    private Tank tank;
    @JsonIgnore
    @CreatedDate
    private LocalDateTime startTime;
    @ReadOnlyProperty
    transient private String remainingTime;
    private Period period;

    public Expedition() { }

    public Expedition(long id, User user, Tank tank, LocalDateTime startTime, Period period) {
        this.id = id;
        this.user = user;
        this.tank = tank;
        this.startTime = startTime;
        this.period = period;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getRemainingTime() {
        LocalDateTime finishTime = startTime.plusHours(period.getHours());
        long period = Duration.between(finishTime, LocalDateTime.now()).toMillis();

        return String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(period),
                TimeUnit.MILLISECONDS.toMinutes(period) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(period))
        );
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public enum Period {
        ONE(1),
        FOUR(4),
        TWELVE(12),
        TWENTY_TWO(24);

        private final int hours;

        Period(int hours) {
            this.hours = hours;
        }

        public int getHours() {
            return hours;
        }
    }
}
