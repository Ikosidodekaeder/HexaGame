package com.hexagon.game.network.packets;

import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.CityBuildings;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketCityBuild extends Packet{

    private Point           arrayPosition;
    private CityBuildings   building;
    private boolean         upgrade = false;


    public PacketCityBuild(Point arrayPosition, CityBuildings building) {
        super(PacketType.CITY_BUILD);
        this.arrayPosition = arrayPosition;
        this.building = building;
    }

    public PacketCityBuild(Point arrayPosition, boolean upgrade) {
        super(PacketType.CITY_BUILD);
        this.arrayPosition = arrayPosition;
        this.building = null;
        this.upgrade = upgrade;
    }

    public PacketCityBuild(UUID clientID, Point arrayPosition, CityBuildings building) {
        super(PacketType.CITY_BUILD, clientID);
        this.arrayPosition = arrayPosition;
        this.building = building;
    }

    public PacketCityBuild(UUID clientID, Point arrayPosition, boolean upgrade) {
        super(PacketType.CITY_BUILD, clientID);
        this.arrayPosition = arrayPosition;
        this.upgrade = upgrade;
    }

    public Point getArrayPosition() {
        return arrayPosition;
    }

    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    public CityBuildings getBuilding() {
        return building;
    }

    public void setBuilding(CityBuildings building) {
        this.building = building;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    @Override
    public String serialize() {
        return super.serialize() + arrayPosition.getX() + "," + arrayPosition.getY() + ";"
                + building.name() + ";" + upgrade + ";";
    }
}
