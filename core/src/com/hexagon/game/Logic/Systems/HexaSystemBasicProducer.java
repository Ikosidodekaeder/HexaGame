package com.hexagon.game.Logic.Systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentProduct;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.system.SystemProducerBase;
import de.svdragster.logica.util.SystemNotifications.Notification;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes Lüke on 20.12.2017.
 */

public class HexaSystemBasicProducer extends SystemProducerBase {

    public HexaSystemBasicProducer(){
        setGlobalEntityContext(Engine.getInstance().getEntityManager());
        subscribe();

    }

    @Override
    public void process(double delta) {
        for(int Entity : getLocalEntityCache().keySet())
        {
            try
            {
                ComponentResource Resource = getEntityResource(Entity);
                if(Resource.productionProgress >= Resource.productionPeriod)
                {
                   EmitProducts(Resource,StdComponents.PRODUCT);
                   ResetProductionProgress(Resource);
                }
                else{
                    //Increase Progress
                    advanceProgress(Resource,(float)delta);
                }
            }catch (NullPointerException e){
                continue;
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if( o instanceof Notification)
        {
            if( o instanceof NotificationNewEntity)
            {
                NotificationNewEntity Note = (NotificationNewEntity) o;
                if(Note.isOfType(StdComponents.PRODUCER)){
                    List<Component> Properties = new ArrayList<>();
                    Properties.add(getGlobalEntityContext().retrieveComponent(Note.Entity(),StdComponents.RESOURCE));
                    getLocalEntityCache()
                            .put(
                                    Note.Entity(),
                                    Properties
                            );
                }

            }
            if( o instanceof NotificationRemoveEntity){
                NotificationRemoveEntity Note = (NotificationRemoveEntity) o;
                getLocalEntityCache().remove(Note.Entity());
            }
        }

    }
}
