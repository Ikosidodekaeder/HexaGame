package com.hexagon.game.map.structures;

/**
 * Created by Sven on 09.03.2018.
 */

public enum CityBuildings {

    BAKERY              ("Bakery", 50, 0, 0.01f, 10),
    SUPERMARKET         ("Supermarket", 150, 0, 0.0125f, 20),
    SCHOOL              ("School", 400, 1, 0.0175f, 40),
    HIGHSCHOOL          ("Highschool", 2000, 1, 0.0225f, 100),
    DISCO               ("Disco", 1000, 2, 0.0275f, 50),
    PUBLIC_TRANSPORT    ("Public Transport", 6_000, 3, 0.03f, 200),
    AIRPORT             ("Airport", 10_000, 4, 0.035f, 300),
    ;

    private String  friendlyName;
    private int     cost;
    private int     minLevel;
    private float   happinessAdd;
    private int     jobs;

    CityBuildings(String friendlyName, int cost, int minLevel, float happinessAdd, int jobs) {
        this.friendlyName   = friendlyName;
        this.cost           = cost;
        this.minLevel       = minLevel;
        this.happinessAdd   = happinessAdd;
        this.jobs           = jobs;
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

    public int getJobs() {
        return jobs;
    }
}
