package com.hexagon.game.network;

import com.badlogic.gdx.graphics.Color;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Sven on 08.03.2018.
 */

public class Player {

    public Color    color;
    public String   username;
    public UUID     uuid;
    public int      claims = 7;
    public long     money = 1000;
    public int      population = 0;
    public int      jobs = HexMap.DEFAULT_JOBS;
    public Map<String, Float> resources = new HashMap<>();

    public transient Point cityLocation;

    public Player(Color color, String username, UUID uuid) {
        this.color = color;
        this.username = username;
        this.uuid = uuid;
        addResource("FOOD", 100);
    }

    @Override
    public String toString() {
        return claims + ","
                + money + ","
                + population + ","
                + jobs + ";";
    }

    public void addResource(String str, float amount) {
        if (!resources.containsKey(str)) {
            resources.put(str, amount);
        } else {
            resources.put(str, resources.get(str) + amount);
        }
    }

    public int getResource(String str) {
        if (resources.containsKey(str)) {
            return (int) Math.floor(resources.get(str));
        }
        return 0;
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
