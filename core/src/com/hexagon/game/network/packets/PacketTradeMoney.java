package com.hexagon.game.network.packets;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.Components.HexaComponentTrade;
import com.hexagon.game.Logic.HexaComponents;

import java.util.UUID;

import de.svdragster.logica.manager.Entity.Entity;

/**
 * Created by Johannes on 12.03.2018.
 */

public class PacketTradeMoney extends Packet {

    public UUID dest;
    public UUID source;
    public HexaComponents type;
    public long originAmount;

    public PacketTradeMoney(UUID dest, UUID source, HexaComponents type, long originAmount){
        super(PacketType.TRADEMONEY);
        this.dest = dest;
        this.source = source;
        this.type = type;
        this.originAmount = originAmount;
    }


    @Override
    public String serialize() {
        if(dest == null){
            return super.serialize()
                    + "" + "," + source + "," + type + ","+ originAmount+";";
        }
        return super.serialize()
                + dest.toString() + "," + source + "," + type + ","+ originAmount+";";
    }
}
