package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarCity;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarCityStats;
import com.hexagon.game.graphics.screens.myscreens.game.InputGame;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;

/**
 * Created by Johannes on 06.03.2018.
 */

public class StateCityView extends State {

    private GroupWindow         groupWindow;
    private SidebarCity         sidebarCity;
    private SidebarCityStats    sidebarCityStats;

    public StateCityView(InputGame input, GameManager gameManager){
        super(StateType.CITY_VIEW, input, gameManager);

        groupWindow = new GroupWindow(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), gameManager.getStage());
        gameManager.getGame().getWindowManager().addWindow(groupWindow);

        sidebarCity = new SidebarCity(groupWindow, gameManager.getStage(), this);

        sidebarCityStats = new SidebarCityStats(groupWindow, gameManager.getStage());


    }

    @Override
    public void render(){

    }

    @Override
    public void logic(){

    }

    @Override
    public void show() {
        sidebarCity.statusWindow.show(gameManager.getStage());
        sidebarCityStats.statusWindow.show(gameManager.getStage());
    }

    @Override
    public void hide() {
        sidebarCity.statusWindow.hide(gameManager.getStage());
        sidebarCityStats.statusWindow.hide(gameManager.getStage());
    }

    @Override
    public void select(HexMap map, Point p, Stage stage) {
        sidebarCity.select(map, p, stage);
        sidebarCityStats.select(map, p, stage);
    }

    @Override
    public void deselect(Stage stage) {
        /*sidebarCity.deselect(stage);
        sidebarCityStats.deselect(stage);
        sidebarCity.statusWindow.hide(gameManager.getStage());
        sidebarCityStats.statusWindow.show(gameManager.getStage());*/
    }
}
