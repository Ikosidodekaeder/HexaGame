package com.hexagon.game.map.structures;

import com.hexagon.game.map.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 05.03.2018.
 */

public class StructureCity extends Structure {

    public static String[] names = new String[] {
            "Nürnberg",
            "Erlangen",
            "Fürth",
            "Schwabach",
            "Frankfurt",
            "Berlin",
            "Augsburg",
            "Ansbach",
            "Stuttgart",
            "Kiel",
            "Magdeburg",
            "Regensburg",
            "Mühlhausen",
            "Forchheim",
            "Düsseldorf",
            "München",
            "Coburg",
            "Weimar",
            "Essen",
            "Erfurt",
            "Neuss",
            "Bamberg",
            "Bremen"
    };

    private transient Point arrayPosition;
    private String name;
    private int level = 0;
    private float population = 500;
    private float happiness = 0.5f;
    private List<CityBuildings> cityBuildingsList = new ArrayList<>();




    public StructureCity() {
        super(StructureType.CITY);
        level = 0;
    }

    public StructureCity(int level) {
        super(StructureType.CITY);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return (int) population;
    }

    public void setPopulation(float population) {
        this.population = population;
    }

    public float getHappiness() {
        return happiness;
    }

    public void setHappiness(float happiness) {
        this.happiness = happiness;
        if (this.happiness < 0.0f) {
            this.happiness = 0.0f;
        } else if (this.happiness > 1.0f) {
            this.happiness = 1.0f;
        }
    }

    public int getMaxPopulation() {
        // ((level + 1)^3)*1000
        return (int) (Math.pow(level + 1, 3)*1000);
    }

    public List<CityBuildings> getCityBuildingsList() {
        return cityBuildingsList;
    }

    public void setCityBuildingsList(List<CityBuildings> cityBuildingsList) {
        this.cityBuildingsList = cityBuildingsList;
    }

    public void calculateHappiness() {
        float newHappiness = 0.5f;
        for (int i=0; i<cityBuildingsList.size(); i++) {
            newHappiness += cityBuildingsList.get(i).getHappinessAdd();
        }
        setHappiness(newHappiness);
    }

    public void update() {
        calculateHappiness();

        float populationAdd = (happiness - 0.5f)*200;
        population += populationAdd;
        int maxPopulation = getMaxPopulation();
        if (population > maxPopulation) {
            population = maxPopulation;
        }
        System.out.println("Updated " + name + " -> " + population);
    }

    public Point getArrayPosition() {
        return arrayPosition;
    }

    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }
}
