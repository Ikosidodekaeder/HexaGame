package com.hexagon.game.network.packets;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Johannes on 02.03.2018.
 */

public class PacketPlayerStatus extends Packet {

    public Map<String,Integer> Stats = new Hashtable<>();
    public UUID PlayerID;

    public PacketPlayerStatus() {
        super(PacketType.PLAYER_STATUS);
    }

    public PacketPlayerStatus(UUID senderId,UUID playerID, Map<String,Integer> stats) {
        super(PacketType.PLAYER_STATUS, senderId);
        this.Stats = stats;
        this.PlayerID = playerID;
    }

    @Override
    public String serialize() {
        StringBuilder   builder = new StringBuilder();
        for(Map.Entry<String,Integer> entry : Stats.entrySet()){
            builder.append(entry).append(",");
        }
        builder.append(";");
        builder.append(PlayerID).append(";");
        return super.serialize() + builder;

    }
}
