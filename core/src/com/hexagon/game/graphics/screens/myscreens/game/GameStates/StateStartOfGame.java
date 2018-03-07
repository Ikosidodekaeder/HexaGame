package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateStartOfGame extends State{

    private InputGame           input;
    private GameManager         gameManager;


    public StateStartOfGame(InputGame input, GameManager gameManager){
        super(StateType.START_OF_GAME, input, gameManager);
    }

    @Override
    public void render(){

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
