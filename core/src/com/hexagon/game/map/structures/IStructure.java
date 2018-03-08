package com.hexagon.game.map.structures;

/**
 * Created by Sven on 08.03.2018.
 */

public interface IStructure {

    // For Gson

    StructureType getType();
    void setType(StructureType type);

}
