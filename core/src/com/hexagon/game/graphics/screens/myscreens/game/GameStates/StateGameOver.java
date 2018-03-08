package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateGameOver extends State {


    public StateGameOver(InputGame input, GameManager gameManager){
        super(StateType.GAME_OVER, input, gameManager);

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
}
