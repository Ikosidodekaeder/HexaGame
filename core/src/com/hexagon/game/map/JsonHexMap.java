package com.hexagon.game.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hexagon.game.map.structures.IStructure;
import com.hexagon.game.map.tiles.Tile;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Sven on 27.02.2018.
 */

public class JsonHexMap {

    private Tile[][] tiles;
    private Map<UUID, String> colors;

    public JsonHexMap(Tile[][] tiles, Map<UUID, String> colors) {
        this.tiles = tiles;
        this.colors = colors;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Map<UUID, String> getColors() {
        return colors;
    }

    public void setColors(Map<UUID, String> colors) {
        this.colors = colors;
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
