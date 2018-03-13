package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.hexagon.game.network.HexaServer;

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


            if (tile.getOwner() == null
                    || !tile.getOwner().equals(HexaServer.senderId)) {
                gameManager.messageUtil.add("You don't own this city!", 4000, Color.RED);
                return;
            }

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
    public void render(ShapeRenderer renderer) {
        //sidebarResourcesWindow.statusWindow.render(renderer);
    }

    @Override
    public void logic() {
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
