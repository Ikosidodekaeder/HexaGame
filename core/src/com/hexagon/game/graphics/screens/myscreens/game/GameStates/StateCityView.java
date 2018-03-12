package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarCity;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarCityStats;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.util.HexagonUtil;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateCityView extends State {

    private GroupWindow         groupWindow;
    private SidebarCity         sidebarCity;
    private SidebarCityStats    sidebarCityStats;
    private Window              hoverWindow; // This displays the city name and level in the middle of the screen

    public StateCityView(InputGame input, GameManager gameManager){
        super(StateType.CITY_VIEW, input, gameManager);

        groupWindow = new GroupWindow(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), gameManager.getStage());
        gameManager.getGame().getWindowManager().addWindow(groupWindow);

        sidebarCity = new SidebarCity(groupWindow, gameManager.getStage(), this);

        sidebarCityStats = new SidebarCityStats(groupWindow, gameManager.getStage());

        hoverWindow = new Window(Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()-200, 400, 100);
        groupWindow.getWindowList().add(hoverWindow);
        hoverWindow.hide(gameManager.getStage());
    }

    @Override
    public void render(ShapeRenderer renderer){

    }

    @Override
    public void logic(){

    }

    @Override
    public void show() {
        sidebarCity.statusWindow.show(gameManager.getStage());
        sidebarCityStats.statusWindow.show(gameManager.getStage());
        hoverWindow.show(gameManager.getStage());
    }

    @Override
    public void hide() {
        sidebarCity.statusWindow.hide(gameManager.getStage());
        sidebarCityStats.statusWindow.hide(gameManager.getStage());

        hoverWindow.hide(gameManager.getStage());

        input.cameraHelper.moveTo(input.cameraLockOnTile, true);
        input.cameraLockOnTile = null;
    }

    @Override
    public void select(HexMap map, Point p, Stage stage) {
        sidebarCity.select(map, p, stage);
        sidebarCityStats.select(map, p, stage);

        if (input.cameraLockOnTile == null) {
            input.cameraLockOnTile = gameManager.getGame().getCamera().position.cpy();
            TileLocation pos = HexagonUtil.getTileLocation(p.getX(), p.getY());
            input.cameraHelper.moveTo(new Vector3((float) pos.getX(), 2, (float) pos.getY()+1), false);
        }
    }

    @Override
    public void deselect(Stage stage) {

        /*sidebarCity.deselect(stage);
        sidebarCityStats.deselect(stage);
        sidebarCity.statusWindow.hide(gameManager.getStage());
        sidebarCityStats.statusWindow.show(gameManager.getStage());*/
    }

    public Window getHoverWindow() {
        return hoverWindow;
    }
}
