package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateGameOver extends State {

    private long lastActionbar = 0;

    public StateGameOver(InputGame input, GameManager gameManager){
        super(StateType.GAME_OVER, input, gameManager);

    }

    @Override
    public void render(ShapeRenderer renderer) {
        if (System.currentTimeMillis() - lastActionbar >= 4000) {
            lastActionbar = System.currentTimeMillis();
            gameManager.messageUtil.actionBar("You have lost the game!", 5000, Color.RED);
        }
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
