package com.hexagon.game.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hexagon.game.map.structures.IStructure;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.Player;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Sven on 27.02.2018.
 *
 */

public class JsonHexMap {

    private Tile[][] tiles;
    private Map<UUID, Player> players;

    public JsonHexMap(Tile[][] tiles, Map<UUID, Player> players) {
        this.tiles = tiles;
        this.players = players;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Map<UUID, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<UUID, Player> players) {
        this.players = players;
    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(IStructure.class, new HexMapAdapter()).create();
        //Gson gson = gsonBuilder.create();
        return gson.toJson(this, JsonHexMap.class);
    }

    public static JsonHexMap fromJson(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.registerTypeAdapter(IStructure.class, new HexMapAdapter());

        Gson gson = gsonBuilder.create();
        JsonHexMap jsonHexMap = gson.fromJson(json, JsonHexMap.class);
        //JsonHexMap jsonHexMap = gson.fromJson(json, new TypeToken<String, Structure>(){}.getType());
        for (int x=0; x<jsonHexMap.getTiles().length; x++) {
            for (int y=0; y<jsonHexMap.getTiles()[x].length; y++) {
                jsonHexMap.getTiles()[x][y].updateTileLocation();
            }
        }
        return jsonHexMap;
    }

}
