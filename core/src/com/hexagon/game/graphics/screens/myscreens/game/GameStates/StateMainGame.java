package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarBuild;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarResources;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateMainGame extends State{

    private GroupWindow         groupWindow;
    private SidebarBuild        sidebarBuildWindow;
    private SidebarResources    sidebarResourcesWindow;

    public StateMainGame(InputGame input, GameManager gameManager) {
        super(StateType.MAIN_GAME, input, gameManager);

        groupWindow = new GroupWindow(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), gameManager.getStage());
        gameManager.getGame().getWindowManager().addWindow(groupWindow);

        Stage stage = gameManager.getStage();

        SidebarBuild sidebar = new SidebarBuild(groupWindow, stage);
        sidebarResourcesWindow = new SidebarResources(groupWindow, stage);

        //sidebar.statusWindow.show(stage);

        sidebarBuildWindow = sidebar;
        sidebar.statusWindow.hide(stage);

    }

    @Override
    public void select(HexMap map, Point p, Stage stage) {
        Tile tile = map.getTileAt(p);

        if (tile.getStructure() != null
                && tile.getStructure().getType() == StructureType.CITY) {
            gameManager.setCurrentState(StateType.CITY_VIEW);
            gameManager.getCurrentState().select(map, p, stage);
            return;
        }
        sidebarBuildWindow.select(map, p, stage);
    }

    @Override
    public void deselect(Stage stage) {
        sidebarBuildWindow.deselect(stage);
    }

    @Override
    public void render() {

    }

    @Override
    public void logic() {
        sidebarResourcesWindow.statusWindow.updateElements();
    }

    @Override
    public void show() {
        sidebarResourcesWindow.statusWindow.show(gameManager.getStage());
    }

    @Override
    public void hide() {
        sidebarResourcesWindow.statusWindow.hide(gameManager.getStage());
    }
}
