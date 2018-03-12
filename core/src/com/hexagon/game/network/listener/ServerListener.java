package com.hexagon.game.network.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.ScreenJoin;
import com.hexagon.game.graphics.screens.myscreens.ScreenMainMenu;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.WindowNotification;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.Player;
import com.hexagon.game.network.packets.PacketBuild;
import com.hexagon.game.network.packets.PacketCityBuild;
import com.hexagon.game.network.packets.PacketDestroy;
import com.hexagon.game.network.packets.PacketJoin;
import com.hexagon.game.network.packets.PacketKeepAlive;
import com.hexagon.game.network.packets.PacketLeave;
import com.hexagon.game.network.packets.PacketPlayerLoaded;
import com.hexagon.game.network.packets.PacketPlayerStatus;
import com.hexagon.game.network.packets.PacketRegister;
import com.hexagon.game.network.packets.PacketServerList;
import com.hexagon.game.network.packets.PacketType;
import com.hexagon.game.util.ConsoleColours;

import java.util.Hashtable;

import de.svdragster.logica.util.Delegate;

/**
 * Created by Sven on 26.02.2018.
 */

public class ServerListener extends PacketListener {

    public long keepAliveSent = 0;

    public ServerListener(HexaServer server) {
        super(server);
    }

    public boolean isBuildableTile(PacketBuild build){
        switch (build.getStructureType()){
            case ORE:{

            }break;
            case CROPS:{

            }break;
            case FOREST:{

            }break;
            default:{

            }
        }

        return false;
    }

    @Override
    public void registerAll() {
        dispatchTable = new Hashtable<PacketType, Delegate>() {{
            put(PacketType.KEEPALIVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    long diff = System.currentTimeMillis() - server.lastKeepAliveSent;
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD + ConsoleColours.PURPLE_BACKGROUND,"Received Keep Alive (" + diff + " ms)");

                    PacketKeepAlive packet = (PacketKeepAlive) args[0];
                    //server.send(new PacketKeepAlive());
                }
            });

            put(PacketType.REGISTER, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketRegister packet = (PacketRegister) args[0];
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD + ConsoleColours.PURPLE_BACKGROUND,"==== RECEIVED REGISTER ====");
                    //System.out.println(ConsoleColours.BLACK_BOLD + "==== RECEIVED REGISTER ==== " + packet.isCancelled() + ConsoleColours.RESET);
                    if (ScreenManager.getInstance().getCurrentScreen().getScreenType() == ScreenType.MAIN_MENU) {
                        ScreenMainMenu mainMenu = (ScreenMainMenu) ScreenManager.getInstance().getCurrentScreen();
                        mainMenu.getWindowManager().removeNotifications(mainMenu.getStage());
                        if (packet.isCancelled()) {
                            new WindowNotification("You are already registered.\n(Please wait a few seconds)", mainMenu.getStage(), mainMenu.getWindowManager());
                        } else {
                            ScreenManager.getInstance().setCurrentScreen(ScreenType.HOST);
                        }
                    }
                }
            });

            put(PacketType.JOIN, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received JOIN" + HexaServer.WhatAmI(server));
                    PacketJoin packet = (PacketJoin) args[0];

                    Player player = new Player(GameManager.instance.colorUtil.getNext(), packet.getUsername());

                    server.getSessionData().addNewPlayer(
                            packet.getSenderId(),
                            packet.getUsername(),
                            player
                    );
                    System.out.println(packet.getUsername() + " has joined the game (I AM THE SERVER)");

                    // I'm the host, so I have to broadcast to my players that a new player has joined the game
                    server.send(new PacketJoin(packet.getUsername(), packet.getSenderId(), packet.getVersion()));

                }
            });

            put(PacketType.LEAVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received LEAVE" + HexaServer.WhatAmI(server));
                    PacketLeave leave = (PacketLeave) args[0];

                    // Confirm the Leave Packet by sending it to the router
                    // (The router sends it to all clients)
                    server.send(new PacketLeave(leave.getLeaverUuid(), leave.isKick()));

                    server.getSessionData().removePlayer(leave.getSenderId());

                }
            });

            put(PacketType.CITY_BUILD, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received CITY_BUILD" + HexaServer.WhatAmI(server));
                    PacketCityBuild packet = (PacketCityBuild) args[0];

                    HexMap map = GameManager.instance.getGame().getCurrentMap();
                    Tile tile = map.getTileAt(packet.getArrayPosition());
                    if (tile.getOwner() == null
                            || !tile.getOwner().equals(packet.getSenderId())) {
                        // The sender may not build in this city
                        return;
                    }

                    // check if the player has enough money to buy

                    if (tile.getStructure() instanceof StructureCity) {
                        StructureCity city = (StructureCity) tile.getStructure();
                        if (!city.getCityBuildingsList().contains(packet.getBuilding())) {
                            city.getCityBuildingsList().add(packet.getBuilding());

                            // Confirm the CityBuild Packet
                            // (The router sends it to all clients)
                            server.send(new PacketCityBuild(packet.getArrayPosition(), packet.getBuilding()));
                        }
                    }



                }
            });

            put(PacketType.BUILD, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received BUILD" + HexaServer.WhatAmI(server));

                    PacketBuild build = (PacketBuild) args[0];


                    // TODO: Check if the player who wants to buildStructure has enough resources to buildStructure
                    // TODO: Check if the player can buildStructure at that location

                    // -> Validate Data to prevent crashes (e.g. when someone sends a corrupted packet)

                    // Let the ClientListener handle the clientsided logic for building
                    //server.getClientListener().call(packetBuild);
                    Tile tile = server.getSessionData().currentMap().getTileAt(build.getArrayPosition());
                    if (tile.getOwner() == null
                            || tile.getOwner().equals(build.getSenderId())) {
                        // Respond
                        server.send(new PacketBuild(
                                        build.getArrayPosition(),
                                        build.getStructureType(),
                                        build.getOwner()
                                )
                        );
                    }


                }
            });

            put(PacketType.DESTROY, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received DESTROY" + HexaServer.WhatAmI(server));

                    PacketDestroy packetDestroy = (PacketDestroy) args[0];

                    // TODO: Check if the player who wants to buildStructure has enough resources to buildStructure
                    // TODO: Check if the player can buildStructure at that location
                    // -> Validate Data to prevent crashes (e.g. when someone sends a corrupted packet)

                    // Let the ClientListener handle the clientsided logic for building
                    //server.getClientListener().call(packetDestroy);

                    // Respond
                    server.send(new PacketDestroy(packetDestroy.getArrayPosition()));

                }
            });

            put(PacketType.TRADE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received TRADE" + HexaServer.WhatAmI(server));

                }
            });

            put(PacketType.TERMINATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received TERMINATE" + HexaServer.WhatAmI(server));

                }
            });

            put(PacketType.MAPUPDATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received UPDATE" + HexaServer.WhatAmI(server));

                }
            });

            put(PacketType.CITY_UPDATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received CITY UPDATE" + HexaServer.WhatAmI(server));

                }
            });

            put(PacketType.SERVER_LIST, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketServerList packetServerList = (PacketServerList) args[0];
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received SERVER_LIST " + packetServerList.entries.size() + HexaServer.WhatAmI(server));
                    if (ScreenManager.getInstance().getCurrentScreen().getScreenType() == ScreenType.JOIN) {
                        final ScreenJoin screenJoin = (ScreenJoin) ScreenManager.getInstance().getCurrentScreen();

                        screenJoin.subwindowServers.removeButtons(screenJoin.getStage());

                        for (final PacketServerList.Entry entry : packetServerList.entries) {
                            screenJoin.subwindowServers.add(new UiButton(entry.room,
                                    30, 0, 100, 40,
                                    screenJoin.getStage(),
                                    new ChangeListener() {
                                        @Override
                                        public void changed(ChangeEvent event, Actor actor) {
                                            System.out.println("Clicked room " + entry.room);
                                            screenJoin.joinRoom(entry.host, entry.room);
                                        }
                                    }), screenJoin.getStage());
                        }

                        screenJoin.subwindowServers.orderAllNeatly(1);
                        screenJoin.subwindowServers.updateElements();
                    }
                }
            });

            put(PacketType.PLAYER_LOADED, new Delegate() {

                private int amount = 0;

                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received PLAYER_LOADED" + HexaServer.WhatAmI(server));
                    PacketPlayerLoaded packet = (PacketPlayerLoaded) args[0];
                    amount++;
                    ConsoleColours.Print(ConsoleColours.WHITE+ConsoleColours.PURPLE_BACKGROUND,"Received PLAYER_LOADED ---> " + amount + HexaServer.WhatAmI(server));
                    System.out.println();

                    if (amount >= 1) {
                        GameManager.instance.server.send(
                                new PacketPlayerLoaded()
                        );
                    }

                }
            });

            put(PacketType.PLAYER_STATUS, new Delegate() {


                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.WHITE_BOLD+ConsoleColours.PURPLE_BACKGROUND,"Received PLAYER_STATUS" + HexaServer.WhatAmI(server));
                    PacketPlayerStatus player = (PacketPlayerStatus)args[0];

                    server.send(new PacketPlayerStatus(
                            HexaServer.senderId,player.PlayerID,player.Stats
                    ));
                    //GameManager.instance.setPlayerResources(player.Stats);

                    /*if(server.isHost() && player.PlayerID.equals( HexaServer.senderId))
                        ;GameManager.instance.setPlayerResources(player.Stats);
                    else
                        server.send(player);*/

                }
            });
        }};
    }
}
