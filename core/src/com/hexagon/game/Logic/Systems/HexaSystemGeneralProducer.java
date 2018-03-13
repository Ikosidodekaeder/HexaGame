package com.hexagon.game.Logic.Systems;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.HexaComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.system.System;
import de.svdragster.logica.system.SystemProducerBase;
import de.svdragster.logica.util.Pair;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationUpdate;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes on 13.02.2018.
 */

public class HexaSystemGeneralProducer extends SystemProducerBase {


    public HexaSystemGeneralProducer(Engine    engine){
        setGlobalEntityContext(engine.getEntityManager());
    }

    @Override
    public void process(double delta) {
        for(int i = 0; i  < getLocalEntityCache().size(); i++){
        //for(Entity e : getLocalEntityCache()){
            //asking if the component is there, is a bit abitrary here because
            //we use the systems cache list of entities.
            Entity e = getLocalEntityCache().get(i);
            Pair<Boolean,ComponentResource> res = e.hasAssociationWith(StdComponents.RESOURCE);
            Pair<Boolean,HexaComponentOwner> owner = e.hasAssociationWith(HexaComponents.OWNER);
            if(res.getFirst() && owner.getFirst()){
                if(this.isReady(res.getSecond())) {
                    //java.lang.System.out.println("===> New Resource: " + res.getSecond().productType + "Owner: "+ owner.getSecond().getID().toString());
                    EmitProducts(res.getSecond(),owner.getSecond());
                    resetProductionProgress(res.getSecond());
                }
                advanceProgress(res.getSecond(), (float) delta);
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {

        if(o instanceof NotificationUpdate){
            List<Entity> global = getGlobalEntityContext().getEntityContext();

        }
        if(o instanceof NotificationNewEntity)
        {
            NotificationNewEntity e = (NotificationNewEntity) o;
            if(e.isOfType(StdComponents.PRODUCER,StdComponents.RESOURCE,HexaComponents.OWNER)){
                this.getLocalEntityCache().add(e.getEntity());
            }
        }
        if(o instanceof NotificationRemoveEntity)
        {
            NotificationRemoveEntity e = (NotificationRemoveEntity) o;
            if(e.isOfType(StdComponents.PRODUCER,StdComponents.RESOURCE,HexaComponents.OWNER)){
                this.getLocalEntityCache().remove(e.getEntity());
            }
        }
    }
}
