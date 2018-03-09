package com.hexagon.game.map.structures;

/**
 * Created by Sven on 09.03.2018.
 */

public enum CityBuildings {

    BAKERY              ("Bakery", 50, 0, 0.015f),
    SUPERMARKET         ("Supermarket", 150, 1, 0.05f),
    SCHOOL              ("School", 400, 2, 0.1f),
    HIGHSCHOOL          ("Highschool", 2000, 3, 0.1f),
    DISCO               ("Disco", 1000, 3, 0.05f),
    PUBLIC_TRANSPORT    ("Public Transport", 5000, 4, 0.1f),
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
