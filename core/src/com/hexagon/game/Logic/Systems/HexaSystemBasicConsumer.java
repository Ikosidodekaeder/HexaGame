package com.hexagon.game.Logic.Systems;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentProduct;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.system.SystemConsumerBase;
import de.svdragster.logica.util.SystemNotifications.Notification;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by z003pksw on 21.12.2017.
 */

public class HexaSystemBasicConsumer extends SystemConsumerBase {

    float period = 1;
    static float progress = 0;


    public HexaSystemBasicConsumer(){
        setGlobalEntityContext(Engine.getInstance().getEntityManager());
        subscribe();
    }
    @Override
    public void process(double v) {

        for(int Entity : getLocalEntityCache().keySet()){
            try{
                ComponentProduct Resource = getEntityResource(Entity);

                if(progress >= period){
                    for(int i = 0; i < 1000; i++)
                        Engine.getInstance().getSystemManager().BroadcastMessage(new NotificationRemoveEntity(Entity));
                    progress=0;
                }

                progress += v;


            }catch(NullPointerException e){

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
                if(Note.isOfType(StdComponents.PRODUCT)){
                    List<Component> Properties = new ArrayList<>();
                    Properties.add(getGlobalEntityContext().retrieveComponent(Note.Entity(),StdComponents.PRODUCT));
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
