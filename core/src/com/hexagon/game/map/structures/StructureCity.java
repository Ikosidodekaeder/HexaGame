package com.hexagon.game.map.structures;

import com.badlogic.gdx.graphics.Color;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sven on 05.03.2018.
 */

public class StructureCity extends Structure {

    private static final float DEFAULT_HAPPINESS = 0.49f;

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
    private float population = 125;
    private float happiness = DEFAULT_HAPPINESS;
    private UUID owner;
    private List<CityBuildings> cityBuildingsList = new ArrayList<>();



    public StructureCity() {
        super(StructureType.CITY);
        level = 0;
    }

    public StructureCity(int level) {
        super(StructureType.CITY);
        this.level = level;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
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
        // ((level + 1)^2)*1000
        return (int) (Math.pow(level + 1, 2)*250);
    }

    public void addBuilding(CityBuildings building) {
        if (cityBuildingsList.contains(building)) {
            return;
        }
        cityBuildingsList.add(building);
    }

    public List<CityBuildings> getCityBuildingsList() {
        return cityBuildingsList;
    }

    public void setCityBuildingsList(List<CityBuildings> cityBuildingsList) {
        this.cityBuildingsList = cityBuildingsList;
    }

    public void calculateHappiness() {
        if (owner == null) return;
        float newHappiness = DEFAULT_HAPPINESS;
        for (int i=0; i<cityBuildingsList.size(); i++) {
            newHappiness += cityBuildingsList.get(i).getHappinessAdd();
        }
        Player player = GameManager.instance.server.getSessionData().PlayerList.get(HexaServer.senderId).getSecond();
        int unemployed = player.population - player.jobs;

        System.out.println(unemployed);
        if (unemployed > 0) {
            newHappiness -= unemployed * 0.0005;
        }

        setHappiness(newHappiness);
    }

    public boolean update() {
        if (owner == null) return false;

        float populationAdd = (happiness - 0.5f)*500;
        population += populationAdd;
        int maxPopulation = getMaxPopulation();
        if (population > maxPopulation) {
            population = maxPopulation;
        } else if (population < 0) {
            population = 0;
            if (owner != null && owner.equals(HexaServer.senderId)) {
                GameManager.instance.messageUtil.add("You lost the game:", 4_000, Color.RED);
                GameManager.instance.messageUtil.add("No population left!", 4_000, Color.RED);
            }
        }
        if (owner != null) {
            GameManager.instance.server.getSessionData().PlayerList.get(owner).getSecond().population += population;
        }
        return true;
    }

    public void upgrade(Tile tile, HexaServer server) {
        tile.getRenderTile().getStructures().clear();
        GameManager.instance.getGame().getCurrentMap().build(
                tile.getArrayX(),
                tile.getArrayY(),
                StructureType.CITY,
                tile.getOwner()
        );
        if (tile.getOwner().equals(HexaServer.senderId)) {
            GameManager.instance.messageUtil.add(getName() + " has been upgraded!", 8000, Color.LIME);
            GameManager.instance.getCurrentState().deselect(GameManager.instance.getStage());
            GameManager.instance.getCurrentState().select(
                    GameManager.instance.getGame().getCurrentMap(),
                    getArrayPosition(),
                    GameManager.instance.getStage()
            );
        } else {
            GameManager.instance.messageUtil.add(
                    server.getSessionData().PlayerList.get(tile.getOwner()).getSecond().username
                            + " has upgraded " + getName(), 8000, Color.RED);
        }
    }

    public int getJobs() {
        int jobs = 0;
        for (int i=0; i<cityBuildingsList.size(); i++) {
            jobs += cityBuildingsList.get(i).getJobs();
        }
        return jobs;
    }

    public Point getArrayPosition() {
        return arrayPosition;
    }

    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }
}
