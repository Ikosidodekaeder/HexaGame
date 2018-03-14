package com.hexagon.game.map.structures;

/**
 * Created by Sven on 13.02.2018.
 */

public enum StructureType {

    FOREST(new String[]{"forest.g3db"}),
    CITY(new String[]{"city_lvl1.g3db", "city_lvl2.g3db", "city_lvl3.g3db", "city_lvl4.g3db", "city_lvl5.g3db"}),
    STREET(new String[]{"street.g3db"}, 0, 50),
    RESOURCE(null),
    ORE(new String[]{"mountain_without_mine.g3db"}),
    MINE(new String[]{"mountain_with_mine.g3db"}, 75, 300),
    FORESTRY(new String[]{"forest.g3db"}, 75, 200),
    QUARRY(new String[]{"mine.g3db"}, 75, 250),
    FACTORY(new String[]{"factory.g3db"}, 125, 1000),
    CROPS(new String[]{"crops.g3db"}, 40, 125);

    private String[] paths;

    private int jobs;
    private int cost;

    StructureType(String[] paths) {
        this.paths = paths;
        this.jobs = 0;
        this.cost = 1;
    }

    StructureType(String[] paths, int jobs, int cost) {
        this.paths = paths;
        this.jobs = jobs;
        this.cost = cost;
    }

    public String[] getPaths() {
        return paths;
    }

    public int getJobs() {
        return jobs;
    }

    public int getCost() {
        return cost;
    }
}
