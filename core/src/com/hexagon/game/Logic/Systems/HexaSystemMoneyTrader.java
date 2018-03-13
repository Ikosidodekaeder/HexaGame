package com.hexagon.game.Logic.Systems;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.Components.HexaComponentTrade;
import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.Logic.Systems.MoneyTraderDetails.GoodConversionRate;
import com.hexagon.game.util.ConsoleColours;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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
        HexaComponentOwner   OwnerCredentials = (HexaComponentOwner)Owner.hasAssociationWith(HexaComponents.OWNER).getSecond();

        ConsoleColours.Print(ConsoleColours.GREEN_BACKGROUND,"Try to collect " +amount + " " + GoodType);
        ConsoleColours.Print(ConsoleColours.GREEN_BACKGROUND,">>> For Player: "+ Owner + " -> " + OwnerCredentials.name);
        for(int j = 0; j < getGlobalEntityContext().getEntityContext().size() ; j++){
            Entity Good = getGlobalEntityContext().getEntityContext().get(j);
            //Check if Entity is a Resource
            String sig = Entity.generateTypeStringFromTypes(
                    GoodType,
                    HexaComponents.OWNER
            );

            if(Good.getComponentSignature().equals(Entity.Signature.calculateSignature(sig))){
                //Check if the Owner of the Resource is OUR owner

                Pair<Boolean,HexaComponentOwner> res = Good.hasAssociationWith(HexaComponents.OWNER);
                ConsoleColours.Print(ConsoleColours.GREEN_BACKGROUND_BRIGHT,"Check Owner " + j);
                if(res.getFirst()){
                    ConsoleColours.Print(ConsoleColours.GREEN_BACKGROUND_BRIGHT,
                            res.getSecond().name + " " + res.getSecond().getID() + " === " + OwnerCredentials.name + " " + OwnerCredentials.getID());
                    if(res.getSecond().getID().equals(OwnerCredentials.getID()))
                    {
                        Goods.add(Good);
                        ConsoleColours.Print("",Good.toString());
                        ConsoleColours.Print(ConsoleColours.GREEN_BACKGROUND_BRIGHT,"OWNER MATCH");
                        if(Goods.size() == amount)
                            break;
                    }
                }
            }
        }

        return Goods;
    }

    HexaComponentOwner changeOwnerOf(Entity source, Entity TheNewOwner){
        HexaComponentOwner NewOwner = (HexaComponentOwner)TheNewOwner.hasAssociationWith(HexaComponents.OWNER).getSecond();
        HexaComponentOwner OldOwner = (HexaComponentOwner)source.hasAssociationWith(HexaComponents.OWNER).getSecond();

        ConsoleColours.Print(ConsoleColours.PURPLE_BACKGROUND_BRIGHT,source + " was owned by >>" + OldOwner.name + "<< will now be owned by >>"
        + NewOwner.name+"<<");

        source.associateComponent(
                NewOwner
        );

        NewOwner.setBackAssociation(source);
        source.generateSignature();
        return OldOwner;
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
                 * resources TO the recipient and therefor ownership is transferred to the GlobalMarket
                 */
                isBuyer = legal.getSecond().OriginAmount >= 0 ;

                if(isBuyer)
                    /*
                        Recipients collects resources in return for Money
                     */
                    goodsOfOrigin = locateGood(
                            legal.getSecond().OriginGood,
                            legal.getSecond().GlobalMarket,
                            (legal.getSecond().OriginAmount)
                    );
                else
                    /*
                        Origin of Trade collects resource for Money
                     */
                    goodsOfOrigin = locateGood(
                            legal.getSecond().OriginGood,
                            legal.getSecond().Origin,
                            (-legal.getSecond().OriginAmount)
                            );


                ConsoleColours.Print(ConsoleColours.RED_BACKGROUND,"Collect GOODS COUNT: " + String.valueOf(goodsOfOrigin.size()));
                if(goodsOfOrigin.size() >= Math.abs(legal.getSecond().OriginAmount) )
                {
                     /*
                            Transfer Ownership of Resources
                     */
                    ConsoleColours.Print(ConsoleColours.PURPLE_BACKGROUND_BRIGHT,"TRANSERFER OF OWNERSHIP IN PROGRESS");
                    ConsoleColours.Print(ConsoleColours.PURPLE_BACKGROUND_BRIGHT,"Collect GOODS COUNT: " + String.valueOf(goodsOfOrigin.size()));
                    if(isBuyer)
                        for(int j = 0; j < goodsOfOrigin.size(); j++)
                            changeOwnerOf(goodsOfOrigin.get(i),legal.getSecond().Origin);
                    else
                        for(int j = 0; j < goodsOfOrigin.size(); j++)
                            changeOwnerOf(goodsOfOrigin.get(i),legal.getSecond().GlobalMarket);


                    if(isBuyer)
                    ( (HexaComponentOwner)legal.getSecond().Origin.hasAssociationWith(HexaComponents.OWNER).getSecond()).money -=
                            (long) (legal.getSecond().OriginAmount * CurrentConversion.rate()) ;
                    else
                        ( (HexaComponentOwner)legal.getSecond().Origin.hasAssociationWith(HexaComponents.OWNER).getSecond()).money +=
                                (long) (legal.getSecond().OriginAmount * CurrentConversion.rate()) ;
                }else{
                    ConsoleColours.Print(ConsoleColours.RED_BACKGROUND,"TRANSFER OF OWNERSHIP NOT POSSIBLE");
                    ConsoleColours.Print(ConsoleColours.RED_BACKGROUND,String.valueOf(goodsOfOrigin.size()));
                    ConsoleColours.Print(ConsoleColours.RED_BACKGROUND,goodsOfOrigin.toString());
                }

                //Remove executed Trade
                engine.BroadcastMessage(new NotificationRemoveEntity(
                        Trade
                ));

            }else{
                /*
                 In Case Of Failure Inform the World about it.
                * */
                ConsoleColours.Print(ConsoleColours.RED_BACKGROUND,"TRADE Unsuccssesfull");
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
                ConsoleColours.Print(ConsoleColours.GREEN_BACKGROUND,"TRADE Recieved");
                this.getLocalEntityCache().add(e.getEntity());
            }
        }
        if(o instanceof NotificationRemoveEntity)
        {
            ConsoleColours.Print(ConsoleColours.GREEN_BACKGROUND,"TRADE Finished");
            NotificationRemoveEntity e = (NotificationRemoveEntity) o;
            if(e.getEntity().hasAnyAssociations())
                if(e.isOfType(HexaComponents.TRADE)){
                    this.getLocalEntityCache().remove(e.getEntity());
                    this.getGlobalEntityContext().removeID(e.getEntity());
                }
        }
    }
}
