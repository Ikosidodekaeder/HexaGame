package com.hexagon.game.network.packets;

import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.CityBuildings;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.network.HexaServer;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public abstract class Packet {

    private PacketType      type;
    private boolean         cancelled;
    /**
     * Issued by the server instance which hosts the game
     */
    private UUID senderId;



     Packet(PacketType type){
        this.type = type;
        this.cancelled = false;
        this.senderId = HexaServer.senderId;
    }

     Packet(PacketType type, UUID senderId){
        this.type = type;
        this.cancelled = false;
        this.senderId = senderId;
    }


    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String serialize() {
        return type.ID + ";"
                + senderId.toString() + ";"
                + (cancelled ? 1 : 0) + ";";
    }

     public static Packet deserialize(String string) {
         String[] arr = string.split(";");
         byte typeId = Byte.parseByte(arr[0]);
         PacketType packetType = PacketType.valueOf(typeId);
         if (packetType == null) {
             return null;
         }

         UUID senderId = UUID.fromString(arr[1]);

         boolean cancelled = arr[2].equals("1");

         if (packetType == PacketType.REGISTER) {
             return new PacketRegister(senderId, arr[3], cancelled); // arr[3] is the room name
         }


         int offset = 3;

         switch (packetType) {
             case KEEPALIVE:
                 int sessionId = Integer.parseInt(arr[offset]);
                 return new PacketKeepAlive(senderId, sessionId);
             case JOIN:
                 UUID hostId = UUID.fromString(arr[offset]);
                 String username = arr[offset+1];
                 String version = arr[offset+2];
                 return new PacketJoin(senderId, username, hostId, version);
             case SERVER_LIST:
                 PacketServerList packetServerList = new PacketServerList(senderId);
                 for (int i=offset; i<arr.length; i++) {
                     String[] strEntry = arr[i].split(",");
                     PacketServerList.Entry entry = new PacketServerList.Entry(
                             UUID.fromString(strEntry[0]),
                             strEntry[1]
                     );
                     packetServerList.entries.add(entry);
                 }
                 return packetServerList;
             case MAPUPDATE:
                 return new PacketMapUpdate(senderId, arr[offset]);
             case BUILD:
                 String[] strPoint = arr[offset].split(",");
                 Point point = new Point(
                         Integer.parseInt(strPoint[0]),
                         Integer.parseInt(strPoint[1])
                 );
                 StructureType structureType = StructureType.valueOf(arr[offset+1]);
                 UUID owner = UUID.fromString(arr[offset+2]);
                 return new PacketBuild(senderId, point, structureType, owner);
             case CITY_BUILD:
                 strPoint = arr[offset].split(",");
                 point = new Point(
                         Integer.parseInt(strPoint[0]),
                         Integer.parseInt(strPoint[1])
                 );
                 if (arr[offset+1].equals("null")) {
                     boolean upgradeCity = arr[offset+2].equals("true");
                     return new PacketCityBuild(senderId, point, upgradeCity);
                 } else {
                     CityBuildings building = CityBuildings.valueOf(arr[offset + 1]);
                     return new PacketCityBuild(senderId, point, building);
                 }
             case CITY_UPDATE: {
                 strPoint = arr[offset].split(",");
                 point = new Point(
                         Integer.parseInt(strPoint[0]),
                         Integer.parseInt(strPoint[1])
                 );
                 if (GameManager.instance.getGame() == null
                         || GameManager.instance.getGame().getCurrentMap() == null) {
                     return null;
                 }
                 HexMap map = GameManager.instance.getGame().getCurrentMap();
                 if (map.getTileAt(point).getStructure() == null
                         || !(map.getTileAt(point).getStructure() instanceof StructureCity)) {
                     return null;
                 }
                 //StructureCity city = (StructureCity) GameManager.instance.getGame().getCurrentMap().getTileAt(point).getStructure();
                 int level = Integer.parseInt(arr[offset + 1]);
                 StructureCity city = new StructureCity(level);
                 String[] buildings = arr[offset + 2].split(",");
                 float happiness = Float.parseFloat(arr[offset + 3]);
                 float population = Float.parseFloat(arr[offset + 4]);

                 city.setLevel(level);
                 city.getCityBuildingsList().clear();
                 for (String strBuilding : buildings) {
                     if (strBuilding.isEmpty()) continue;
                     CityBuildings building = CityBuildings.valueOf(strBuilding);
                     city.getCityBuildingsList().add(building);
                 }
                 city.setHappiness(happiness);
                 city.setPopulation(population);

                 return new PacketCityUpdate(point, city);
             }
             case DESTROY:
                 strPoint = arr[offset].split(",");
                 point = new Point(
                         Integer.parseInt(strPoint[0]),
                         Integer.parseInt(strPoint[1])
                 );
                 return new PacketDestroy(senderId, point);
             case LEAVE:
                 UUID leaverUuid = UUID.fromString(arr[offset]);
                 boolean kick = arr[offset+1].equals("true");
                 return new PacketLeave(senderId, leaverUuid, kick);
             case HOST_GENERATING:
                 return new PacketHostGenerating(senderId);
             case PLAYER_LOADED:
                 return new PacketPlayerLoaded(senderId);
             case PLAYER_STATUS:{
                 String[] values = arr[offset].split(",");
                 Map<String,Float> payload = new Hashtable<>();
                 for (String stringResource : values) {
                     String[] arrResource = stringResource.split("=");
                     payload.put(arrResource[0], Float.parseFloat(arrResource[1]));
                 }
                 UUID playerId = UUID.fromString(arr[offset+1]);

                 String[] strPlayer = arr[offset+2].split(",");
                 int  claims        = Integer.parseInt(strPlayer[0]);
                 long money         = Integer.parseInt(strPlayer[1]);
                 int  population    = Integer.parseInt(strPlayer[2]);
                 int  jobs          = Integer.parseInt(strPlayer[3]);

                 return new PacketPlayerStatus(senderId, playerId, payload, claims, money, population, jobs);
             }
             case TRADEMONEY:{
                 String[] values = arr[offset].split(",");
                 if(values[0].length() == 0)
                     return new PacketTradeMoney(
                             null,
                             UUID.fromString(values[1]),
                             HexaComponents.valueOf(values[2]),
                             Integer.parseInt(values[3])
                     );
                 return new PacketTradeMoney(
                         UUID.fromString(values[0]),
                         UUID.fromString(values[1]),
                         HexaComponents.valueOf(values[2]),
                         Integer.parseInt(values[3])
                 );
             }
         }

         return null;
     }
}
