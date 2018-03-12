package com.hexagon.game.Logic.Components;

import com.hexagon.game.Logic.HexaComponents;

import java.util.UUID;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.manager.Entity.Entity;

/**
 * Created by Johannes on 08.03.2018.
 */

public class HexaComponentTrade extends Component {

    public Entity                  Recipient;
    public Entity                  Origin;

    public HexaComponents          OriginGood;
    public long                    OriginAmount;

    public HexaComponentTrade(){
        setType(HexaComponents.TRADE);
    }

    public HexaComponentTrade(Entity dest,Entity source,HexaComponents type,long originAmount){
        this.Recipient = dest;
        this.Origin = source;
        this.OriginGood = type;
        this.OriginAmount = originAmount;
        setType(HexaComponents.TRADE);
    }


    public String ToString(){
        return ((UUID)Recipient.hasAssociationWith(HexaComponents.OWNER).getSecond()).toString()
                + ","
                + ((UUID)Origin.hasAssociationWith(HexaComponents.OWNER).getSecond()).toString()
                +","
                +OriginGood
                +","
                +OriginAmount;
    }
}
