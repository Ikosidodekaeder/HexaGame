package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateCityView;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateType;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;

/**
 * Created by Sven on 01.03.2018.
 */

public class SidebarCity extends Sidebar {

    private StateCityView owner;

    public SidebarCity(GroupWindow window, Stage stage, StateCityView owner) {
        super(window, stage, Gdx.graphics.getHeight()/2+5, Gdx.graphics.getHeight()/2+5, Gdx.graphics.getHeight()/2-60);
        this.owner = owner;
    }

    @Override
    public void select(final HexMap map, final Point p, final Stage stage) {
        super.select(map, p, stage);
        statusWindow.removeAll(stage);

        Tile tile = map.getTileAt(p.getX(), p.getY());
        if (tile.getStructure() == null
                || tile.getStructure().getType() != StructureType.CITY) {
            return;
        }
        if (!(tile.getStructure() instanceof StructureCity)) {
            System.err.println("ERROR SidebarCity: Structure not an instance of StructureCity even though its type is CITY");
            return;
        }

        StructureCity city = (StructureCity) tile.getStructure();

        cityLevel(stage, city);
        exitCityView(stage);

        for (UiElement element : statusWindow.getElementList()) {
            element.setHeight(element.getHeight() + 10);
        }
        statusWindow.orderAllNeatly(1);
        statusWindow.updateElements();

    }

    private void cityLevel(final Stage stage, StructureCity city) {
        UILabel label = new UILabel(5, 0, 50, 0, 32, "City Level " + city.getLevel());
        statusWindow.add(label, stage);

    }

    private void exitCityView(Stage stage) {
        UiButton exit = new UiButton("Exit City View", 5, 0, 50, 0, 26);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                owner.getGameManager().setCurrentState(StateType.MAIN_GAME);
            }
        });
        statusWindow.add(exit, stage);
    }

}
