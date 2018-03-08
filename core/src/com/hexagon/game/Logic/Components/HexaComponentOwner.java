package com.hexagon.game.Logic.Components;

import com.hexagon.game.Logic.HexaComponents;


import java.util.UUID;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.manager.Entity.Entity;

/**
 * Created by Johannes on 13.02.2018.
 */

public class HexaComponentOwner extends Component {

    public String   name;
    public long     money = 0;

    public HexaComponentOwner(String Name){
        super.setType(HexaComponents.OWNER);

        name = Name;
    }

    public HexaComponentOwner(String Name,UUID id){
        super(id);
        super.setType(HexaComponents.OWNER);

        name = Name;
    }

    public void setOwner(Entity e){ setBackAssociation(e);}

    @Override
    public String toString(){
        return "Owner( " + name + " ) => " + super.getID();
    }
}
