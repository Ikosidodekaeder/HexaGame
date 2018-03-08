package com.hexagon.game.Logic.Systems;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.Components.HexaComponentTrade;
import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.Logic.Systems.MoneyTraderDetails.GoodConversionRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.system.System;
import de.svdragster.logica.util.Pair;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes on 08.03.2018.
 */

public class HexaSystemMoneyTrader extends System {

    ArrayList<GoodConversionRate>           ConversionData = new ArrayList<>();
    GoodConversionRate                      CurrentConversion;
    Engine                                  engine;

    public HexaSystemMoneyTrader(Engine engine){
        this.engine = engine;
        setGlobalEntityContext(engine.getEntityManager());

        ConversionData.add(
                new GoodConversionRate(
                        HexaComponents.STONE,
                        100,
                        100
                )
        );

        ConversionData.add(
                new GoodConversionRate(
                        HexaComponents.WOOD,
                        100,
                        100
                )
        );

        ConversionData.add(
                new GoodConversionRate(
                        HexaComponents.ORE,
                        100,
                        100
                )
        );
    }

    Pair<Boolean,HexaComponentTrade> isTradePossible(Entity Trade){
        for( GoodConversionRate rate : ConversionData){
            Pair<Boolean,HexaComponentTrade> trade = Trade.hasAssociationWith(HexaComponents.TRADE);
            if(trade.getFirst()) {
                if(trade.getSecond().OriginGood.equals(rate.getGood()))
                {
                    CurrentConversion = rate;
                    return trade;
                }
            }
        }

        return new Pair<>(false,null);
    }

    List<Entity> locateGood(HexaComponents GoodType, Entity Owner,long amount){
        ArrayList<Entity>    Goods = new ArrayList<>();


        for(int j = 0; j < getGlobalEntityContext().getEntityContext().size(); j++){
            Entity Good = getGlobalEntityContext().getEntityContext().get(j);

            //Check if Entity is a Resource
            if(Good.getComponentSignature().equals(Entity.generateTypeStringFromTypes(
                    HexaComponents.OWNER,
                    GoodType
            ))){
                //Check if the Owner of the Resource is OUR owner
                Pair<Boolean,HexaComponentOwner> res = Good.hasAssociationWith(HexaComponents.OWNER);
                if(res.getFirst()){
                    HexaComponentOwner ow = (HexaComponentOwner)Owner.hasAssociationWith(HexaComponents.OWNER).getSecond();
                    if(res.getSecond().getID().equals(ow.getID()))
                        Goods.add(Good);
                }
            }
        }

        return Goods;
    }

    void changeOwner(Entity source,Entity owner){
        source.associateComponent(
                (HexaComponentOwner)owner.hasAssociationWith(HexaComponents.OWNER).getSecond()
        );
    }

    @Override
    public void process(double delta) {

        boolean isBuyer = true;
        List<Entity>  goodsOfOrigin;

        for(int i = 0; i < getLocalEntityCache().size(); i++){
            Entity Trade = getLocalEntityCache().get(i);

            Pair<Boolean,HexaComponentTrade> legal = isTradePossible(Trade);
            if(legal.getFirst()){

                /**
                 * IF the amount of goods is positiv the Recepient is writting over Ownership
                 * of the resource entities to the Origin of the Trade BUT
                 * IF the amount of goods is negative the Origin of the Trade sells its
                 * resources TO the recipient and therefor ownership is transferred to the Recipient
                 */
                isBuyer = legal.getSecond().OriginAmount > 0 ? true : false;

                if(isBuyer)
                    /*
                        Recipients collects resources in return for Money
                     */
                    goodsOfOrigin = locateGood(
                            legal.getSecond().OriginGood,
                            legal.getSecond().Recipient,
                            (isBuyer ? legal.getSecond().OriginAmount : -legal.getSecond().OriginAmount)
                    );
                else
                    /*
                        Origin of Trade collects resource for Money
                     */
                    goodsOfOrigin = locateGood(
                            legal.getSecond().OriginGood,
                            legal.getSecond().Origin,
                            (isBuyer ? legal.getSecond().OriginAmount : -legal.getSecond().OriginAmount)
                            );

                if(goodsOfOrigin.size() >= (isBuyer ? legal.getSecond().OriginAmount : -legal.getSecond().OriginAmount) )
                {
                     /*
                            Transfer Ownership of Resources
                     */
                    if(isBuyer)
                        for(int j = 0; j < goodsOfOrigin.size(); j++)
                            changeOwner(goodsOfOrigin.get(i),legal.getSecond().Recipient);
                    else
                        for(int j = 0; j < goodsOfOrigin.size(); j++)
                            changeOwner(goodsOfOrigin.get(i),legal.getSecond().Origin);


                    if(isBuyer)
                    ( (HexaComponentOwner)legal.getSecond().Origin.hasAssociationWith(HexaComponents.OWNER).getSecond()).money +=
                            (long) (legal.getSecond().OriginAmount * CurrentConversion.rate()) ;
                    else
                        ( (HexaComponentOwner)legal.getSecond().Origin.hasAssociationWith(HexaComponents.OWNER).getSecond()).money -=
                                (long) (legal.getSecond().OriginAmount * CurrentConversion.rate()) ;
                }

                //Remove executed Trade
                engine.BroadcastMessage(new NotificationRemoveEntity(
                        Trade
                ));

            }else{
                /*
                 In Case Of Failure Inform the World about it.
                * */
                engine.BroadcastMessage(
                        null
                );

            }
        }

    }

    @Override
    public void update(Observable observable, Object o) {

        if(o instanceof NotificationNewEntity)
        {
            NotificationNewEntity e = (NotificationNewEntity) o;
            if(e.isOfType(HexaComponents.TRADE)){
                this.getLocalEntityCache().add(e.getEntity());
            }
        }
        if(o instanceof NotificationRemoveEntity)
        {
            NotificationRemoveEntity e = (NotificationRemoveEntity) o;
            if(e.getEntity().hasAnyAssociations())
                if(e.isOfType(HexaComponents.TRADE)){
                    this.getLocalEntityCache().remove(e.getEntity());
                }
        }
    }
}
