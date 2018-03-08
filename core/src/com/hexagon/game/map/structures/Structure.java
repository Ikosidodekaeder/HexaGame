package com.hexagon.game.map.structures;

/**
 * Structures are for example Trees, Buildings, Rocks...
 *
 * Created by Sven on 08.12.2017.
 */

public class Structure implements IStructure {

    private StructureType type;

    public Structure() {
        // Gson needs this constructor
    }

    public Structure(StructureType type) {
        this.type = type;
    }

    @Override
    public StructureType getType() {
        return type;
    }

    @Override
    public void setType(StructureType type) {
        this.type = type;
    }
}
