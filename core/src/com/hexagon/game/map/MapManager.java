package com.hexagon.game.map;

import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.tiles.Tile;

/**
 * Created by Sven on 21.12.2017.
 */

public class MapManager {

    private static MapManager instance;

    private HexMap currentHexMap;

    public MapManager() {
        instance = this;
    }

    public HexMap getCurrentHexMap() {
        return currentHexMap;
    }

    public void setCurrentHexMap(HexMap currentHexMap) {
        this.currentHexMap = currentHexMap;
        for (int x=0; x<currentHexMap.getTiles().length; x++) {
            for (int y=0; y<currentHexMap.getTiles()[x].length; y++) {
                Tile tile = currentHexMap.getTileAt(x, y);
                if (tile.getStructure() != null
                        && tile.getStructure() instanceof StructureCity) {
                    StructureCity city = (StructureCity) tile.getStructure();
                    city.setArrayPosition(new Point(x, y));
                    currentHexMap.getCities().add(city);
                }
            }
        }
    }

    public static MapManager getInstance() {
        return instance;
    }

}
