package com.hexagon.game.network.packets;

import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.CityBuildings;
import com.hexagon.game.map.structures.StructureCity;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketCityUpdate extends Packet{

    private Point           arrayPosition;
    private StructureCity   city;


    public PacketCityUpdate(Point arrayPosition, StructureCity city) {
        super(PacketType.CITY_UPDATE);
        this.arrayPosition = arrayPosition;
        this.city = city;
    }

    public PacketCityUpdate(UUID clientID, Point arrayPosition, StructureCity city) {
        super(PacketType.CITY_UPDATE, clientID);
        this.arrayPosition = arrayPosition;
        this.city = city;
    }

    public Point getArrayPosition() {
        return arrayPosition;
    }

    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    public StructureCity getCity() {
        return city;
    }

    public void setCity(StructureCity city) {
        this.city = city;
    }

    @Override
    public String serialize() {
        StringBuilder buildings = new StringBuilder();
        for (CityBuildings building : city.getCityBuildingsList()) {
            buildings.append(building.name()).append(",");
        }
        return super.serialize() + arrayPosition.getX() + "," + arrayPosition.getY() + ";"
                + city.getLevel() + ";"
                + buildings.toString() + ";"
                + city.getHappiness() + ";"
                + city.getPopulation() + ";";
    }
}
