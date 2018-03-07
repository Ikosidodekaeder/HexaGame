package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarBuild;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateMainGame extends State{

    private GroupWindow     groupWindow;
    private SidebarBuild    sidebarBuildWindow;

    public StateMainGame(InputGame input, GameManager gameManager) {
        super(StateType.MAIN_GAME, input, gameManager);

        groupWindow = new GroupWindow(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), gameManager.getStage());

        Stage stage = gameManager.getStage();

        SidebarBuild sidebar = new SidebarBuild(groupWindow, stage);

        //sidebar.statusWindow.show(stage);

        sidebarBuildWindow = sidebar;
        sidebar.statusWindow.hide(stage);
    }

    @Override
    public void select(HexMap map, Point p, Stage stage) {
        //Tile tile = map.getTileAt(p);
        sidebarBuildWindow.select(map, p, stage);
    }

    @Override
    public void deselect(Stage stage) {
        sidebarBuildWindow.deselect(stage);
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
