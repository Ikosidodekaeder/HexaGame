package com.hexagon.game.ai;

import com.badlogic.gdx.graphics.Color;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateType;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.network.Player;
import com.hexagon.game.network.packets.PacketBuild;

import java.util.UUID;

/**
 * Created by Sven on 25.03.2018.
 */

public class ComputerPlayer extends Player {

    private StateType currentState;
    private long      wait;

    public ComputerPlayer(Color color, String username, UUID uuid) {
        super(color, username, uuid);

        this.currentState = StateType.START_OF_GAME;
        sleep(7_500);
    }

    public void update() {
        if (System.currentTimeMillis() < wait) return;

        if (currentState == StateType.START_OF_GAME) {
            claimCity();
            return;
        }
    }

    private void claimCity() {
        GameManager.instance.messageUtil.add(username + " claiming city");
        HexMap map = GameManager.instance.getGame().getCurrentMap();

        for (StructureCity city : map.getCities()) {
            if (city.getOwner() == null) {
                GameManager.instance.server.send(new PacketBuild(
                        city.getArrayPosition(),
                        StructureType.CITY,
                        uuid
                ));
                break;
            }
        }

        currentState = StateType.MAIN_GAME;
        sleep(5000);
    }

    private void sleep(long ms) {
        this.wait = System.currentTimeMillis() + ms;
    }
}
