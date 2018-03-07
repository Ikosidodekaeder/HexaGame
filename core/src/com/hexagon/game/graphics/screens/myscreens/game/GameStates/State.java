package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;

/**
 * Created by Johannes on 06.03.2018.
 */

public abstract class State {

    StateType stateType;
    InputGame input;
    GameManager gameManager;

    public State(StateType stateType, InputGame input, GameManager gameManager) {
        this.stateType = stateType;
        this.input = input;
        this.gameManager = gameManager;
    }

    public abstract void render();
    public abstract void logic();

    public abstract void show();
    public abstract void hide();

    public void select(HexMap map, Point p, Stage stage) {

    }

    public void deselect(Stage stage) {

    }

    public StateType getStateType() {
        return stateType;
    }
}
