package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateCityView extends State{

    private InputGame           input;
    private GameManager         gameManager;


    public StateCityView(InputGame input, GameManager gameManager){
        super(StateType.CITY_VIEW, input, gameManager);

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
