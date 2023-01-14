package com.aaronr92.tanksgame.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.ReadOnlyProperty;

@Entity
public class Tank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ReadOnlyProperty
    private long id;
    private String name;
    private int price;
    private String description;
    @Column(name = "tank_class")
    @Enumerated(EnumType.STRING)
    private Class tankClass;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Nation nation;
    private int level;

    public Tank() {}

    public Tank(String name, int price, String description, Class tankClass, Type type, Nation nation, int level) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.tankClass = tankClass;
        this.type = type;
        this.nation = nation;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public Tank setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tank setName(String name) {
        this.name = name;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public Tank setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Tank setDescription(String description) {
        this.description = description;
        return this;
    }

    public Class getTankClass() {
        return tankClass;
    }

    public Tank setTankClass(Class tankClass) {
        this.tankClass = tankClass;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Tank setType(Type type) {
        this.type = type;
        return this;
    }

    public Nation getNation() {
        return nation;
    }

    public Tank setNation(Nation nation) {
        this.nation = nation;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Tank setLevel(int level) {
        this.level = level;
        return this;
    }

    public enum Class {
        LIGHT,
        MEDIUM,
        HEAVY,
        TANK_DESTROYER,
        ARTILLERY;
    }

    public enum Type {
        PREMIUM,
        DEFAULT,
        COLLECTIBLE;
    }

    public enum Nation {
        USSR("СССР"),
        GERMANY("Германия"),
        CHINA("Китай"),
        USA("США"),
        FRANCE("Франция"),
        UK("Великобритания"),
        JAPAN("Япония"),
        CZECHOSLOVAKIA("Чехословакия"),
        SWEDEN("Швеция"),
        ITALY("Италия");

        private final String country;

        Nation(String country) {
            this.country = country;
        }

        public String getCountry() {
            return country;
        }
    }
}
