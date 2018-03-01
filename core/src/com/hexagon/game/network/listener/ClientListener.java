package com.hexagon.game.network.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.ScreenJoin;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.JsonHexMap;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.map.Point;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketBuild;
import com.hexagon.game.network.packets.PacketDestroy;
import com.hexagon.game.network.packets.PacketJoin;
import com.hexagon.game.network.packets.PacketKeepAlive;
import com.hexagon.game.network.packets.PacketLeave;
import com.hexagon.game.network.packets.PacketMapUpdate;
import com.hexagon.game.network.packets.PacketServerList;
import com.hexagon.game.network.packets.PacketType;

import java.util.Hashtable;

import de.svdragster.logica.util.Delegate;

/**
 * Created by Sven on 26.02.2018.
 */

public class ClientListener extends PacketListener {


    public ClientListener(HexaServer server) {
        super(server);
    }

    @Override
    public void registerAll() {
        dispatchTable = new Hashtable<PacketType, Delegate>() {{
            put(PacketType.KEEPALIVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketKeepAlive packet = (PacketKeepAlive) args[0];
                    //server.send(new PacketKeepAlive());
                }
            });

            put(PacketType.JOIN, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Client Received JOIN");
                    PacketJoin packet = (PacketJoin) args[0];

                    // I'm not the host, so either a new player has joined the game or I have joined the game
                    System.out.println(HexaServer.senderId.toString() + " ////// " + packet.getHostId().toString());
                    if (packet.getHostId().equals(HexaServer.senderId)) {
                        System.out.println("==== YOU HAVE JOINED THE GAME!! (Username: " + packet.getUsername() + ")");
                        ScreenManager.getInstance().setCurrentScreen(ScreenType.LOBBY);
                    } else {
                        System.out.println(packet.getUsername() + " has joined the game");
                    }

                }
            });

            put(PacketType.LEAVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketLeave packetLeave = (PacketLeave) args[0];
                    // TODO: Get username somehow
                    System.out.println("USER has left the game");

                }
            });

            put(PacketType.BUILD, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketBuild packetBuild = (PacketBuild) args[0];
                    System.out.println("Client Received BUILD " + packetBuild.getArrayPosition().getX() + ", " + packetBuild.getArrayPosition().getY()
                        + " -> " + packetBuild.getStructureType().name());

                    PacketBuild destroy = (PacketBuild) args[0];
                    Point pos = destroy.getArrayPosition();
                    HexMap map = GameManager.instance.getGame().getCurrentMap();

                    map.build(pos.getX(), pos.getY(), packetBuild.getStructureType());

                }
            });

            put(PacketType.DESTROY, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Client Received DESTROY");

                    PacketDestroy destroy = (PacketDestroy) args[0];
                    Point pos = destroy.getArrayPosition();
                    HexMap map = GameManager.instance.getGame().getCurrentMap();

                    map.deconstruct(pos.getX(), pos.getY());
                }
            });

            put(PacketType.TRADE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Client Received TRADE");

                }
            });

            put(PacketType.TERMINATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Client Received TERMINATE");

                }
            });

            put(PacketType.MAPUPDATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Client Received MAPUPDATE");
                    PacketMapUpdate packetMapUpdate = (PacketMapUpdate) args[0];
                    HexMap hexMap;

                    // If the player is playing offline, the packet contains a list of tiles
                    // If the player is connected to the server it will only contain the raw json
                    if (packetMapUpdate.getTiles() != null) {
                        hexMap = new HexMap(
                                packetMapUpdate.getTiles().length,
                                (packetMapUpdate.getTiles().length == 0) ? (0) : (packetMapUpdate.getTiles()[0].length)
                        );
                        hexMap.setTiles(packetMapUpdate.getTiles());
                    } else {
                        String json = packetMapUpdate.getRawMapData();

                        JsonHexMap jsonHexMap = JsonHexMap.fromJson(json);

                        hexMap = new HexMap(
                                jsonHexMap.getTiles().length,
                                (jsonHexMap.getTiles().length == 0) ? (0) : (jsonHexMap.getTiles()[0].length)
                        );
                        System.out.println("HEX MAP " + hexMap.getTiles().length);
                        hexMap.setTiles(jsonHexMap.getTiles());
                    }
                    MapManager.getInstance().setCurrentHexMap(hexMap);

                    if (ScreenManager.getInstance().getCurrentScreen().getScreenType() != ScreenType.GAME) {
                        ScreenManager.getInstance().setCurrentScreen(ScreenType.GAME);
                    }

                }
            });

            put(PacketType.SERVER_LIST, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketServerList packetServerList = (PacketServerList) args[0];
                    System.out.println("Client Received SERVER_LIST " + packetServerList.entries.size());
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
        }}
        ;
    }
}