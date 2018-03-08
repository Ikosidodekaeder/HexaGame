package com.hexagon.game.Logic.Components;

import com.hexagon.game.Logic.HexaComponents;

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
}
