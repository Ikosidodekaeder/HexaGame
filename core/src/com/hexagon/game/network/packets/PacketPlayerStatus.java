package com.hexagon.game.network.packets;

import com.hexagon.game.network.Player;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Johannes on 02.03.2018.
 */

public class PacketPlayerStatus extends Packet {

    public Map<String, Integer> Stats = new Hashtable<>();
    public int claims;
    public long money;
    public int population;
    public int jobs;
    public UUID     PlayerID;

    public PacketPlayerStatus() {
        super(PacketType.PLAYER_STATUS);
    }

    public PacketPlayerStatus(UUID senderId,UUID playerID, Map<String,Integer> stats,
                              int claims, long money, int population, int jobs) {
        super(PacketType.PLAYER_STATUS, senderId);
        this.Stats      = stats;
        this.PlayerID   = playerID;
        this.claims     = claims;
        this.money      = money;
        this.population = population;
        this.jobs       = jobs;
    }

    public PacketPlayerStatus(UUID senderId,UUID playerID, Map<String,Integer> stats, Player player) {
        this(senderId, playerID, stats, player.claims, player.money, player.population, player.jobs);
    }

    @Override
    public String serialize() {
        StringBuilder   builder = new StringBuilder();
        for(Map.Entry<String,Integer> entry : Stats.entrySet()){
            builder.append(entry).append(",");
        }
        builder.append(";");
        builder.append(PlayerID)    .append(";");
        builder.append(claims)      .append(",");
        builder.append(money)       .append(",");
        builder.append(population)  .append(",");
        builder.append(jobs)        .append(",").append(";");
        return super.serialize() + builder;

    }
}
