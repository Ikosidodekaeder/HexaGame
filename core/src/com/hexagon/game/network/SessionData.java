package com.hexagon.game.network;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.util.ConsoleColours;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.util.Pair;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes on 19.02.2018.
 */

public class SessionData implements SessionActions {
    public Map<UUID,Pair<Entity,Player>>        PlayerList = new Hashtable<>();

    public HexMap   currentMap(){
        return GameManager.instance.getGame().getCurrentMap();
    }

    public synchronized void  addNewPlayer(UUID playerID,String Name,Player player){

        if(!PlayerList.containsKey(playerID)){
            PlayerList.put(
                    playerID,
                    new Pair<Entity, Player>(Engine.getInstance().getEntityManager()
                            .createID(
                                    new HexaComponentOwner(Name,playerID))
                    , player));
        }else
            throw new RuntimeException("User already in Game");
    }

    public synchronized void removePlayer(UUID   player){
        if(!PlayerList.containsKey(player)){
            Engine.getInstance().BroadcastMessage(
                    new NotificationRemoveEntity(PlayerList.get(player).getFirst())
            );
            PlayerList.remove(player);
        }else{
            throw new RuntimeException("User does not exist and therefor can not be removed");
        }
    }

    public synchronized void buildStructure(UUID playerID, StructureType type){

        if(!PlayerList.containsKey(playerID)){

            switch (type){
                case ORE:{

                }break;
            }
            Engine.getInstance().BroadcastMessage(
                    new NotificationRemoveEntity(PlayerList.get(playerID).getFirst())
            );
        }else{
            throw new RuntimeException("User does not exist and therefor can not be removed");
        }
    }

    public synchronized Map<String,Integer> getPlayerResourceStatus(UUID playerID) throws RuntimeException{
        if(PlayerList.containsKey(playerID)){

            ConsoleColours.Print(ConsoleColours.CYAN_BACKGROUND+ConsoleColours.BLACK, "Start collecting Information for: " + playerID + "This is done here: "+ HexaServer.WhatAmI(GameManager.instance.server));
            Map<String,Integer> result = PlayerList.get(playerID).getSecond().resources;

            /*List<List<Component>> Components = Engine.getInstance().getComponentManager().groupByTypes(
                    HexaComponents.ORE,HexaComponents.WOOD,HexaComponents.STONE
            );

            for(List<Component> Resource : Components){
               for(Component c :  Resource){
                   Entity player = c.getBackAssociation();

                   Pair<Boolean,HexaComponentOwner> Owner = player.hasAssociationWith(HexaComponents.OWNER);
                   if(Owner.getFirst() && (Owner.getSecond().getID().equals(playerID))){
                       HexaComponents type = (HexaComponents)c.getType();
                       if(result.containsKey(type.name()))
                           result.put(type.name(),result.get(type.name())+1);
                       else
                           result.put(type.name(),0);
                   }
               }
            }
            if(result.isEmpty()){
                ConsoleColours.Print(ConsoleColours.CYAN_BACKGROUND+ConsoleColours.BLACK,">>>>>>>>>>>>>>>>> I HATE YOU: no data found for player: " + playerID);
                return new Hashtable<String,Integer>() {{
                    put("STONE",0);
                    put("WOOD",-1);
                    put("ORE",0);
                }};
            }
            if(!result.containsKey("WOOD"))
            {
                ConsoleColours.Print(ConsoleColours.CYAN_BACKGROUND+ConsoleColours.BLACK,">>>>>>>>>>>>>>>>> I HATE YOU: no WOOD found for player: " + playerID);

                result.put("WOOD",0);
            }
            if(!result.containsKey("STONE"))
            {
                ConsoleColours.Print(ConsoleColours.CYAN_BACKGROUND+ConsoleColours.BLACK,">>>>>>>>>>>>>>>>> I HATE YOU: no STONE found for player: "  + playerID);

                result.put("STONE",0);
            }
            if(!result.containsKey("ORE"))
            {
                ConsoleColours.Print(ConsoleColours.CYAN_BACKGROUND+ConsoleColours.BLACK,">>>>>>>>>>>>>>>>> I HATE YOU: no ORE found for player: "  + playerID);

                result.put("ORE",0);
            }*/
            return result;
        }

        throw new RuntimeException("Player was not part of the game Session!!!!!!!!!!!!!!!!!!!!!");
    }

}
