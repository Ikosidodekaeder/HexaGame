package com.hexagon.game.map.structures;

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
            "Mühlhausen"
    };

    private String name;
    private int level = 0;


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
}
