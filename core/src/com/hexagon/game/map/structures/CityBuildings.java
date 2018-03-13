package com.hexagon.game.map.structures;

/**
 * Created by Sven on 09.03.2018.
 */

public enum CityBuildings {

    BAKERY              ("Bakery", 50, 0, 0.0075f),
    SUPERMARKET         ("Supermarket", 150, 0, 0.01f),
    SCHOOL              ("School", 400, 1, 0.01f),
    HIGHSCHOOL          ("Highschool", 2000, 1, 0.01f),
    DISCO               ("Disco", 1000, 2, 0.0075f),
    PUBLIC_TRANSPORT    ("Public Transport", 5_000, 3, 0.01f),
    AIRPORT             ("Airport", 10_000, 4, 0.015f),
    ;

    private String friendlyName;
    private int cost;
    private int minLevel;
    private float happinessAdd;

    CityBuildings(String friendlyName, int cost, int minLevel, float happinessAdd) {
        this.friendlyName = friendlyName;
        this.cost = cost;
        this.minLevel = minLevel;
        this.happinessAdd = happinessAdd;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public int getCost() {
        return cost;
    }

    public float getHappinessAdd() {
        return happinessAdd;
    }

    public int getMinLevel() {
        return minLevel;
    }
}
