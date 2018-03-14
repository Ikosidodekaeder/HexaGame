package com.hexagon.game.network;

import com.badlogic.gdx.graphics.Color;
import com.hexagon.game.map.HexMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sven on 08.03.2018.
 */

public class Player {

    public Color    color;
    public String   username;
    public int      claims = 5;
    public long     money = 1000;
    public int      population = 0;
    public int      jobs = HexMap.DEFAULT_JOBS;
    public Map<String, Integer> resources = new HashMap<>();

    public Player(Color color, String username) {
        this.color = color;
        this.username = username;
        addResource("FOOD", 100);
    }

    @Override
    public String toString() {
        return claims + ","
                + money + ","
                + population + ","
                + jobs + ";";
    }

    public void addResource(String str, int amount) {
        if (!resources.containsKey(str)) {
            resources.put(str, amount);
        } else {
            resources.put(str, resources.get(str) + amount);
        }
    }

    public boolean hasResource(String str, int amount) {
        return resources.containsKey(str) && resources.get(str) >= amount;
    }

    public void removeResource(String str, int amount) {
        if (resources.containsKey(str)) {
            resources.put(str, resources.get(str) - amount);
        }
    }
}
