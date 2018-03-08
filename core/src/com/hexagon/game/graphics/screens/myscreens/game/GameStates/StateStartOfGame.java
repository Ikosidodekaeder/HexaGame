package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketBuild;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateStartOfGame extends State{


    public StateStartOfGame(InputGame input, GameManager gameManager){
        super(StateType.START_OF_GAME, input, gameManager);
    }

    @Override
    public void render(ShapeRenderer renderer){

    }

    @Override
    public void logic(){

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void select(HexMap map, Point p, Stage stage) {
        Tile tile = map.getTileAt(p);
        if (tile.getStructure() != null
                && tile.getStructure().getType() == StructureType.CITY) {
            gameManager
                    .server
                    .send(new PacketBuild(
                    p,
                    StructureType.CITY,
                    HexaServer.senderId
            ));
        } else {
            GameManager.instance.messageUtil.actionBar("Please select a town to begin...", 15_000, Color.GREEN);
        }
    }
}
